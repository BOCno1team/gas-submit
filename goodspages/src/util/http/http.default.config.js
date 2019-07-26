  // mport qs from 'qs'

const httpConfig = {
  // timeout: 5000,
  contentType: 'application/json',
  baseUrl: '',

  reqInterceptors: (config) => {
    // POST传参序列化
    // if (config.method === 'post') {
    //   config.data = qs.stringify(config.data);
    // }
    return config;
  },

  resInterceptors: (res) => {
    return res;
  }

}

export default httpConfig
