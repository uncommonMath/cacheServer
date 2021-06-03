package cacheServer;

import cacheServer.core.CacheManager;
import cacheServer.predicating.impl.PredicateEqual;
import cacheServer.predicating.impl.PredicateNot;
import cacheServer.property.PropertyName;
import cacheServer.property.template.PropertySample;
import cacheServer.property.typing.PropertyRawValue;
import cacheServer.property.typing.PropertyTypeRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        var test = new CacheManager();
        test.
                beginInit().
                with(PropertyTypeRepository.PROPERTY_TYPE_REPOSITORY.get("Integer"),
                        new PropertyName("cost")
                ).
                with(PropertyTypeRepository.PROPERTY_TYPE_REPOSITORY.get("Float"),
                        new PropertyName("quality")
                ).
                with(new PropertyName("cost")).
            end();

        test.
                beginAdd().
                with(new PropertyName("cost"), new PropertyRawValue("300")).
                with(new PropertyName("quality"), new PropertyRawValue("11.2")).
            end();

        test.
                beginAdd().
                with(new PropertyName("cost"), new PropertyRawValue("299")).
                with(new PropertyName("quality"), new PropertyRawValue("11.2")).
            end();

        test.
                beginAdd().
                with(new PropertyName("cost"), new PropertyRawValue("301")).
                with(new PropertyName("quality"), new PropertyRawValue("11.2")).
                end();

        var query = test.
                beginGet().
                    //with(new PropertyName("quality"), new PropertyRawValue("11.2")).
                    with(new PredicateNot(new PredicateEqual(new PropertySample(
                            new PropertyName("cost"),
                            "300")))).
                end();

        for (var r: (List<?>)query)
        {
            System.out.println(r);
        }

        test.
                beginRemove().
                with(new PropertyName("cost"), new PropertyRawValue("299")).
            end();

        System.out.println();

        query = test.
                beginGet().
                with(new PropertyName("quality"), new PropertyRawValue("11.2")).
                end();

        for (var r: (List<?>)query)
        {
            System.out.println(r);
        }

        System.out.println(test);
    }
}
