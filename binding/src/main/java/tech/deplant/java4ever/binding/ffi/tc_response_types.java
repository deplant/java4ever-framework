package tech.deplant.java4ever.binding.ffi;

/**
 * {@snippet lang = c:
    enum tc_response_types_t {
      tc_response_success = 0,
      tc_response_error = 1,
      tc_response_nop = 2,
      tc_response_app_request = 3,
      tc_response_app_notify = 4,
      tc_response_custom >= 100,
    };
 *}
 */
enum tc_response_types {

	TC_RESPONSE_SUCCESS,
	TC_RESPONSE_ERROR,
	TC_RESPONSE_NOP,
	TC_RESPONSE_APP_REQUEST,
	TC_RESPONSE_APP_NOTIFY,
	TC_RESPONSE_CUSTOM,
	TC_RESPONSE_RESERVED;

	public static tc_response_types of(int val) {
		tc_response_types result;
		if (val == 0) {
			result = TC_RESPONSE_SUCCESS;
		} else if (val == 1) {
			result = TC_RESPONSE_ERROR;
		} else if (val == 2) {
			result = TC_RESPONSE_NOP;
		} else if (val == 3) {
			result = TC_RESPONSE_APP_REQUEST;
		} else if (val == 4) {
			result = TC_RESPONSE_APP_NOTIFY;
		} else if (val >= 100) {
			result = TC_RESPONSE_CUSTOM;
		} else {
			result = TC_RESPONSE_RESERVED;
		}
		return result;
	}
}
