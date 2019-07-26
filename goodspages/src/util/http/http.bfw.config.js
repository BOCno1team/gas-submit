import qs from 'qs'
import CU from '../commonutil'
import store from '@/store'

const bfwConfig = {
  timeout: 5000,
  contentType: 'application/x-www-form-urlencoded',
  baseUrl: '',

  reqInterceptors: (config) => {
    var locale = store.state.locale;
    var method = config.url;
    config.url = '/api/_bfwajax.do';
    // POST传参序列化
    if(config.method === 'post') {
      var data = {
        method: method,
        params: config.data,
        header: {
          agent: "BFE",
          locale: locale
        }
      };

      var json = {
        json: JSON.stringify(data)
      };
      config.data = qs.stringify(json);
      if (config.params) {
        config.params["method"] = method;
        config.params["_locale"] = locale;
      } else {
        config.params = {
          method: method,
          _locale: locale
        }
      }
    }
    return config;
  },

  resInterceptors: (res) => {
    if (res.status === 200) {
      if (CU.getErr(res) && CU.getErr(res).code === '0107') {
        store.dispatch('logout');
      }
    }
    return res;
  }

}

export default bfwConfig
