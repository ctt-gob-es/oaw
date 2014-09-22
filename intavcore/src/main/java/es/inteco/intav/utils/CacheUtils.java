package es.inteco.intav.utils;

import com.opensymphony.oscache.base.EntryRefreshPolicy;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import com.opensymphony.oscache.web.filter.ExpiresRefreshPolicy;
import es.inteco.common.IntavConstants;
import es.inteco.common.properties.PropertiesManager;

public class CacheUtils {
    private static GeneralCacheAdministrator gca = null;

    public static GeneralCacheAdministrator getCacheAdministratorInstance() {
        if (gca == null) {
            PropertiesManager pmgr = new PropertiesManager();
            gca = new GeneralCacheAdministrator(pmgr.getProperties(IntavConstants.CACHEINTAV_PROPERTIES));
        }

        return gca;
    }

    public static void putInCache(Object object, String cacheKey) {
        PropertiesManager pmgr = new PropertiesManager();
        EntryRefreshPolicy entryRefreshPolicy = new ExpiresRefreshPolicy(Integer.parseInt(pmgr.getValue(IntavConstants.CACHEINTAV_PROPERTIES, "cache.refresh.days")) * 86400);
        try {
            getCacheAdministratorInstance().putInCache(cacheKey, object, entryRefreshPolicy);
        } catch (Exception e) {
            getCacheAdministratorInstance().cancelUpdate(cacheKey);
        }
    }

    public static void putInCacheForever(Object object, String cacheKey) {
        try {
            getCacheAdministratorInstance().putInCache(cacheKey, object);
        } catch (Exception e) {
            getCacheAdministratorInstance().cancelUpdate(cacheKey);
        }
    }

    public static Object getFromCache(String cacheKey) throws NeedsRefreshException {
        return getCacheAdministratorInstance().getFromCache(cacheKey);
    }

    public static void removeFromCache(String cacheKey) {
        getCacheAdministratorInstance().removeEntry(cacheKey);
    }

    public static void cancelUpdate(String cacheKey) {
        getCacheAdministratorInstance().cancelUpdate(cacheKey);
    }

}
