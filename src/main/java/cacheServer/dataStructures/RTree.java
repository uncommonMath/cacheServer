package cacheServer.dataStructures;

import java.util.*;

@SuppressWarnings("unused")
public final class RTree<T> {
    public enum SeedPicker {
        LINEAR
    }

    private final int minEntries;
    private final int maxEntries;
    private final int numDims;
    private final float[] pointDims;
    private final SeedPicker seedPicker;
    private Node root;
    private int size;

    private static final float DIM_FACTOR = -2.0f;
    private static final float FUDGE_FACTOR = 1.001f;

    private Node buildRoot(final boolean asLeaf) {
        float[] initCoords = new float[numDims];
        float[] initDimensions = new float[numDims];
        for (int i = 0; i < this.numDims; i++) {
            initCoords[i] = (float) Math.sqrt(Float.MAX_VALUE);
            initDimensions[i] = DIM_FACTOR * (float) Math.sqrt(Float.MAX_VALUE);
        }
        return new Node(initCoords, initDimensions, asLeaf);
    }

    public RTree(final int minEntries, final int maxEntries, final int numDims,
                 final SeedPicker seedPicker) {
        assert (minEntries <= (maxEntries / 2));
        this.minEntries = minEntries;
        this.maxEntries = maxEntries;
        this.numDims = numDims;
        pointDims = new float[numDims];
        this.seedPicker = seedPicker;
        root = buildRoot(true);
    }

    public RTree(final int minEntries, final int maxEntries, final int numDims) {
        this(minEntries, maxEntries, numDims, SeedPicker.LINEAR);
    }

    public RTree() {
        this(2, 50, 2, SeedPicker.LINEAR);
    }

    public int getMinEntries() {
        return minEntries;
    }

    public int getMaxEntries() {
        return maxEntries;
    }

    public int getNumDims() {
        return numDims;
    }

    public int size() {
        return size;
    }

    public void clear() {
        root = buildRoot(true);
    }

    public List<T> search(final float[] coords, final float[] dimensions) {
        assert (coords.length == numDims);
        assert (dimensions.length == numDims);
        LinkedList<T> results = new LinkedList<>();
        search(coords, dimensions, root, results);
        return results;
    }

    private void search(final float[] coords, final float[] dimensions, final Node n,
                        final LinkedList<T> results) {
        if (n.leaf) {
            for (Node e : n.children) {
                if (isOverlap(coords, dimensions, e.coords, e.dimensions)) {
                    //noinspection unchecked
                    results.add(((Entry<T>) e).entry);
                }
            }
        } else {
            for (Node c : n.children) {
                if (isOverlap(coords, dimensions, c.coords, c.dimensions)) {
                    search(coords, dimensions, c, results);
                }
            }
        }
    }

    public boolean delete(final float[] coords, final float[] dimensions, final T entry) {
        assert (coords.length == numDims);
        assert (dimensions.length == numDims);
        Node l = findLeaf(root, coords, dimensions, entry);
        if (l == null) {
            throw new RuntimeException("leaf not found for entry " + entry);
        }
        ListIterator<Node> li = l.children.listIterator();
        T removed = null;
        while (li.hasNext()) {
            @SuppressWarnings("unchecked")
            Entry<T> e = (Entry<T>) li.next();
            if (e.entry.equals(entry)) {
                removed = e.entry;
                li.remove();
                break;
            }
        }
        if (removed != null) {
            condenseTree(l);
            size--;
        }
        if (size == 0) {
            root = buildRoot(true);
        }
        return (removed != null);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean delete(final float[] coords, final T entry) {
        return delete(coords, pointDims, entry);
    }

    private Node findLeaf(final Node n, final float[] coords,
                          final float[] dimensions, final T entry) {
        if (n.leaf) {
            for (Node c : n.children) {
                //noinspection rawtypes
                if (((Entry) c).entry.equals(entry)) {
                    return n;
                }
            }
        } else {
            for (Node c : n.children) {
                if (isOverlap(c.coords, c.dimensions, coords, dimensions)) {
                    Node result = findLeaf(c, coords, dimensions, entry);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    private void condenseTree(final Node pn) {
        Node n = pn;
        Set<Node> q = new HashSet<>();
        while (n != root) {
            if (n.leaf && (n.children.size() < minEntries)) {
                q.addAll(n.children);
                n.parent.children.remove(n);
            } else if (!n.leaf && (n.children.size() < minEntries)) {
                LinkedList<Node> toVisit = new LinkedList<>(n.children);
                while (!toVisit.isEmpty()) {
                    Node c = toVisit.pop();
                    if (c.leaf) {
                        q.addAll(c.children);
                    } else {
                        toVisit.addAll(c.children);
                    }
                }
                n.parent.children.remove(n);
            } else {
                tighten(n);
            }
            n = n.parent;
        }
        if (root.children.size() == 0) {
            root = buildRoot(true);
        } else if ((root.children.size() == 1) && (!root.leaf)) {
            root = root.children.get(0);
            root.parent = null;
        } else {
            tighten(root);
        }
        for (Node ne : q) {
            @SuppressWarnings("unchecked")
            Entry<T> e = (Entry<T>) ne;
            insert(e.coords, e.dimensions, e.entry);
        }
        size -= q.size();
    }

    public void insert(final float[] coords, final float[] dimensions, final T entry) {
        assert (coords.length == numDims);
        assert (dimensions.length == numDims);
        Entry<T> e = new Entry<>(coords, dimensions, entry);
        Node l = chooseLeaf(root, e);
        l.children.add(e);
        size++;
        e.parent = l;
        if (l.children.size() > maxEntries) {
            Node[] splits = splitNode(l);
            adjustTree(splits[0], splits[1]);
        } else {
            adjustTree(l, null);
        }
    }

    public void insert(final float[] coords, final T entry) {
        insert(coords, pointDims, entry);
    }

    private void adjustTree(final Node n, final Node nn) {
        if (n == root) {
            if (nn != null) {
                root = buildRoot(false);
                root.children.add(n);
                n.parent = root;
                root.children.add(nn);
                nn.parent = root;
            }
            tighten(root);
            return;
        }
        tighten(n);
        if (nn != null) {
            tighten(nn);
            if (n.parent.children.size() > maxEntries) {
                Node[] splits = splitNode(n.parent);
                adjustTree(splits[0], splits[1]);
            }
        }
        if (n.parent != null) {
            adjustTree(n.parent, null);
        }
    }

    private Node[] splitNode(final Node n) {
        Node[] nn = new Node[]{n, new Node(n.coords, n.dimensions, n.leaf)};
        nn[1].parent = n.parent;
        if (nn[1].parent != null) {
            nn[1].parent.children.add(nn[1]);
        }
        LinkedList<Node> cc = new LinkedList<>(n.children);
        n.children.clear();
        Node[] ss = seedPicker == SeedPicker.LINEAR ? lPickSeeds(cc) : qPickSeeds(cc);
        nn[0].children.add(ss[0]);
        nn[1].children.add(ss[1]);
        tighten(nn);
        while (!cc.isEmpty()) {
            if ((nn[0].children.size() >= minEntries)
                    && (nn[1].children.size() + cc.size() == minEntries)) {
                nn[1].children.addAll(cc);
                cc.clear();
                tighten(nn);
                return nn;
            } else if ((nn[1].children.size() >= minEntries)
                    && (nn[0].children.size() + cc.size() == minEntries)) {
                nn[0].children.addAll(cc);
                cc.clear();
                tighten(nn);
                return nn;
            }
            Node c = seedPicker == SeedPicker.LINEAR ? lPickNext(cc) : qPickNext(cc, nn);
            Node preferred;
            float e0 = getRequiredExpansion(nn[0].coords, nn[0].dimensions, c);
            float e1 = getRequiredExpansion(nn[1].coords, nn[1].dimensions, c);
            if (e0 < e1) {
                preferred = nn[0];
            } else if (e0 > e1) {
                preferred = nn[1];
            } else {
                float a0 = getArea(nn[0].dimensions);
                float a1 = getArea(nn[1].dimensions);
                if (a0 < a1) {
                    preferred = nn[0];
                } else if (e0 > a1) {
                    preferred = nn[1];
                } else {
                    if (nn[0].children.size() < nn[1].children.size()) {
                        preferred = nn[0];
                    } else if (nn[0].children.size() > nn[1].children.size()) {
                        preferred = nn[1];
                    } else {
                        preferred = nn[(int) Math.round(Math.random())];
                    }
                }
            }
            preferred.children.add(c);
            tighten(preferred);
        }
        return nn;
    }

    private Node[] qPickSeeds(final LinkedList<Node> nn) {
        Node[] bestPair = new Node[2];
        float maxWaste = -1.0f * Float.MAX_VALUE;
        for (Node n1 : nn) {
            for (Node n2 : nn) {
                if (n1 == n2) {
                    continue;
                }
                float n1a = getArea(n1.dimensions);
                float n2a = getArea(n2.dimensions);
                float ja = 1.0f;
                for (int i = 0; i < numDims; i++) {
                    float jc0 = Math.min(n1.coords[i], n2.coords[i]);
                    float jc1 = Math.max(n1.coords[i] + n1.dimensions[i], n2.coords[i] + n2.dimensions[i]);
                    ja *= (jc1 - jc0);
                }
                float waste = ja - n1a - n2a;
                if (waste > maxWaste) {
                    maxWaste = waste;
                    bestPair[0] = n1;
                    bestPair[1] = n2;
                }
            }
        }
        nn.remove(bestPair[0]);
        nn.remove(bestPair[1]);
        return bestPair;
    }

    private Node qPickNext(final LinkedList<Node> cc, final Node[] nn) {
        float maxDiff = -1.0f * Float.MAX_VALUE;
        Node nextC = null;
        for (Node c : cc) {
            float n0Exp = getRequiredExpansion(nn[0].coords, nn[0].dimensions, c);
            float n1Exp = getRequiredExpansion(nn[1].coords, nn[1].dimensions, c);
            float diff = Math.abs(n1Exp - n0Exp);
            if (diff > maxDiff) {
                maxDiff = diff;
                nextC = c;
            }
        }
        assert (nextC != null) : "No node selected from qPickNext";
        cc.remove(nextC);
        return nextC;
    }

    private Node[] lPickSeeds(final LinkedList<Node> nn) {
        Node[] bestPair = new Node[2];
        boolean foundBestPair = false;
        float bestSep = 0.0f;
        for (int i = 0; i < numDims; i++) {
            float dimLb = Float.MAX_VALUE, dimMinUb = Float.MAX_VALUE;
            float dimUb = -1.0f * Float.MAX_VALUE, dimMaxLb = -1.0f * Float.MAX_VALUE;
            Node nMaxLb = null, nMinUb = null;
            for (Node n : nn) {
                if (n.coords[i] < dimLb) {
                    dimLb = n.coords[i];
                }
                if (n.dimensions[i] + n.coords[i] > dimUb) {
                    dimUb = n.dimensions[i] + n.coords[i];
                }
                if (n.coords[i] > dimMaxLb) {
                    dimMaxLb = n.coords[i];
                    nMaxLb = n;
                }
                if (n.dimensions[i] + n.coords[i] < dimMinUb) {
                    dimMinUb = n.dimensions[i] + n.coords[i];
                    nMinUb = n;
                }
            }
            float sep = (nMaxLb == nMinUb) ? -1.0f
                    : Math.abs((dimMinUb - dimMaxLb) / (dimUb - dimLb));
            if (sep >= bestSep) {
                bestPair[0] = nMaxLb;
                bestPair[1] = nMinUb;
                bestSep = sep;
                foundBestPair = true;
            }
        }
        if (!foundBestPair) {
            bestPair = new Node[]{nn.get(0), nn.get(1)};
        }
        nn.remove(bestPair[0]);
        nn.remove(bestPair[1]);
        return bestPair;
    }

    private Node lPickNext(final LinkedList<Node> cc) {
        return cc.pop();
    }

    private void tighten(final Node... nodes) {
        assert (nodes.length >= 1) : "Pass some nodes to tighten!";
        for (Node n : nodes) {
            assert (n.children.size() > 0) : "tighten() called on empty node!";
            float[] minCoords = new float[numDims];
            float[] maxCoords = new float[numDims];
            for (int i = 0; i < numDims; i++) {
                minCoords[i] = Float.MAX_VALUE;
                maxCoords[i] = Float.MIN_VALUE;

                for (Node c : n.children) {
                    c.parent = n;
                    if (c.coords[i] < minCoords[i]) {
                        minCoords[i] = c.coords[i];
                    }
                    if ((c.coords[i] + c.dimensions[i]) > maxCoords[i]) {
                        maxCoords[i] = (c.coords[i] + c.dimensions[i]);
                    }
                }
            }
            for (int i = 0; i < numDims; i++) {
                maxCoords[i] -= minCoords[i];
            }
            System.arraycopy(minCoords, 0, n.coords, 0, numDims);
            System.arraycopy(maxCoords, 0, n.dimensions, 0, numDims);
        }
    }

    private Node chooseLeaf(final Node n, final Entry<T> e) {
        if (n.leaf) {
            return n;
        }
        float minInc = Float.MAX_VALUE;
        Node next = null;
        for (Node c : n.children) {
            float inc = getRequiredExpansion(c.coords, c.dimensions, e);
            if (inc < minInc) {
                minInc = inc;
                next = c;
            } else if (inc == minInc) {
                float curArea = 1.0f;
                float thisArea = 1.0f;
                for (int i = 0; i < c.dimensions.length; i++) {
                    assert next != null;
                    curArea *= next.dimensions[i];
                    thisArea *= c.dimensions[i];
                }
                if (thisArea < curArea) {
                    next = c;
                }
            }
        }
        assert next != null;
        return chooseLeaf(next, e);
    }

    private float getRequiredExpansion(final float[] coords, final float[] dimensions, final Node e) {
        float area = getArea(dimensions);
        float[] deltas = new float[dimensions.length];
        for (int i = 0; i < deltas.length; i++) {
            if (coords[i] + dimensions[i] < e.coords[i] + e.dimensions[i]) {
                deltas[i] = e.coords[i] + e.dimensions[i] - coords[i] - dimensions[i];
            } else if (coords[i] + dimensions[i] > e.coords[i] + e.dimensions[i]) {
                deltas[i] = coords[i] - e.coords[i];
            }
        }
        float expanded = 1.0f;
        for (int i = 0; i < dimensions.length; i++) {
            area *= dimensions[i] + deltas[i];
        }
        return (expanded - area);
    }

    private float getArea(final float[] dimensions) {
        float area = 1.0f;
        for (float dimension : dimensions) {
            area *= dimension;
        }
        return area;
    }

    private boolean isOverlap(final float[] scoords, final float[] sdimensions,
                              final float[] coords, final float[] dimensions) {

        for (int i = 0; i < scoords.length; i++) {
            boolean overlapInThisDimension = false;
            if (scoords[i] == coords[i]) {
                overlapInThisDimension = true;
            } else if (scoords[i] < coords[i]) {
                if (scoords[i] + FUDGE_FACTOR * sdimensions[i] >= coords[i]) {
                    overlapInThisDimension = true;
                }
            } else if (scoords[i] > coords[i]) {
                if (coords[i] + FUDGE_FACTOR * dimensions[i] >= scoords[i]) {
                    overlapInThisDimension = true;
                }
            }
            if (!overlapInThisDimension) {
                return false;
            }
        }
        return true;
    }

    private static class Node {
        final float[] coords;
        final float[] dimensions;
        Node parent;
        final LinkedList<Node> children;
        final boolean leaf;

        private Node(float[] coords, float[] dimensions, boolean leaf) {
            this.coords = new float[coords.length];
            this.dimensions = new float[dimensions.length];
            System.arraycopy(coords, 0, this.coords, 0, coords.length);
            System.arraycopy(dimensions, 0, this.dimensions, 0, dimensions.length);
            this.leaf = leaf;
            children = new LinkedList<>();
        }
    }

    private static class Entry<T> extends Node {
        public final T entry;

        public Entry(final float[] coords, final float[] dimensions, final T entry) {
            super(coords, dimensions, true);
            this.entry = entry;
        }

        @Override
        public String toString() {
            return "Entry: " + entry;
        }
    }
}