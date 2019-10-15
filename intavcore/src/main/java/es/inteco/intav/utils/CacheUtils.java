/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.intav.utils;

import com.opensymphony.oscache.base.EntryRefreshPolicy;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import com.opensymphony.oscache.web.filter.ExpiresRefreshPolicy;
import es.inteco.common.IntavConstants;
import es.inteco.common.properties.PropertiesManager;

/**
 * The Class CacheUtils.
 */
public final class CacheUtils {
    
    /** The gca. */
    private static GeneralCacheAdministrator gca = null;

    /**
	 * Instantiates a new cache utils.
	 */
    private CacheUtils() {
    }

    /**
	 * Gets the cache administrator instance.
	 *
	 * @return the cache administrator instance
	 */
    public static GeneralCacheAdministrator getCacheAdministratorInstance() {
        if (gca == null) {
            PropertiesManager pmgr = new PropertiesManager();
            gca = new GeneralCacheAdministrator(pmgr.getProperties(IntavConstants.CACHEINTAV_PROPERTIES));
        }

        return gca;
    }

    /**
	 * Put in cache.
	 *
	 * @param object   the object
	 * @param cacheKey the cache key
	 */
    public static void putInCache(Object object, String cacheKey) {
        PropertiesManager pmgr = new PropertiesManager();
        EntryRefreshPolicy entryRefreshPolicy = new ExpiresRefreshPolicy(Integer.parseInt(pmgr.getValue(IntavConstants.CACHEINTAV_PROPERTIES, "cache.refresh.days")) * 86400);
        try {
            getCacheAdministratorInstance().putInCache(cacheKey, object, entryRefreshPolicy);
        } catch (Exception e) {
            getCacheAdministratorInstance().cancelUpdate(cacheKey);
        }
    }

    /**
	 * Put in cache forever.
	 *
	 * @param object   the object
	 * @param cacheKey the cache key
	 */
    public static void putInCacheForever(Object object, String cacheKey) {
        try {
            getCacheAdministratorInstance().putInCache(cacheKey, object);
        } catch (Exception e) {
            getCacheAdministratorInstance().cancelUpdate(cacheKey);
        }
    }

    /**
	 * Gets the from cache.
	 *
	 * @param cacheKey the cache key
	 * @return the from cache
	 * @throws NeedsRefreshException the needs refresh exception
	 */
    public static Object getFromCache(String cacheKey) throws NeedsRefreshException {
        return getCacheAdministratorInstance().getFromCache(cacheKey);
    }

    /**
	 * Removes the from cache.
	 *
	 * @param cacheKey the cache key
	 */
    public static void removeFromCache(String cacheKey) {
        getCacheAdministratorInstance().removeEntry(cacheKey);
    }

    /**
	 * Cancel update.
	 *
	 * @param cacheKey the cache key
	 */
    public static void cancelUpdate(String cacheKey) {
        getCacheAdministratorInstance().cancelUpdate(cacheKey);
    }

}
