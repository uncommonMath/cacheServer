package cacheServer;

import cacheServer.core.CacheManager;
import cacheServer.property.PropertyName;
import cacheServer.property.typing.PropertyRawValue;
import cacheServer.property.typing.PropertyTypeRepository;

public class TestCacheSize {
    private static CacheManager fillCache(int count)
    {
        var cacheManager = new CacheManager();
        cacheManager.beginInit().with(
                PropertyTypeRepository.PROPERTY_TYPE_REPOSITORY.get("Integer"),
                new PropertyName("cost")
        ).with(new PropertyName("cost")).end();
        for (var j = 0; j < count; j++)
        {
            cacheManager.beginAdd().with(new PropertyName("cost"), new PropertyRawValue("300")).end();
        }
        return cacheManager;
    }

    public static void main(String[] args) throws InterruptedException {
        for (var i = 1; i < 45; i++)
        {
            var count = (int)30000f * i;
            System.gc();
            Thread.sleep(10000);
            var cache = fillCache(count);
            System.out.printf("Size for %d objects = %d bytes\n", count,
                    Runtime.getRuntime().totalMemory());
        }
    }
}
