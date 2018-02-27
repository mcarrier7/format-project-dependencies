/**
 * 
 */
package formatter.dependencies.converters;

import org.apache.commons.beanutils.ConvertUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Used for general purpose value conversion via appache commons ConvertUtils
 */
public class ValueConverter
{
	/**
	 * Private constructor to prevent accidental instantiation
	 */
	private ValueConverter() {}

	/**
	 * Convert value into the specified type
	 * 
	 * @param <X>
	 *            Class to convert to
	 * @param value
	 *            Value to convert from
	 * @param targetType
	 *            Type to convert into
	 * @return The converted value
	 */
	@SuppressWarnings("unchecked")
	public static <X> X convert(
			Object value,
			Class<X> targetType ) {
		if (value != null) {
			// if object is already in intended target type, no need to convert
			// it, just return as it is
			if (value.getClass() == targetType) {
				return (X) value;
			}

			if (value.getClass() == JSONObject.class || value.getClass() == JSONArray.class) {
				return (X) value;
			}
		}

		String strValue = String.valueOf(value);
		Object retval = ConvertUtils.convert(
				strValue,
				targetType);
		return (X) retval;
	}
}
