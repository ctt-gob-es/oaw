package es.inteco.multilanguage.converter;

import es.inteco.common.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;

import java.math.BigDecimal;
import java.sql.Date;

public class GenericConverter implements Converter {

    static {
        ConvertUtils.register(new BigDecimalConverter(BigDecimal.ZERO), BigDecimal.class);
        ConvertUtils.register(new SqlDateConverter(new Date(0)), Date.class);
    }

    @Override
    public Object convert(Class clazz, Object value) {

        if (value != null) {
            Object object = null;
            try {
                object = (Class.forName(clazz.getName())).newInstance();
                BeanUtils.copyProperties(object, value);
            } catch (Exception e) {
                Logger.putLog("Error al realizar la conversión", GenericConverter.class, Logger.LOG_LEVEL_ERROR, e);
            }

            return object;
        } else {
            return null;
        }
    }
}
