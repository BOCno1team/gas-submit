// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import store from './store'
import axios from 'axios'
import VueHttp from './util/http/vue-http'
import CommonUtil from './util/commonutil'

Vue.use(ElementUI);
Vue.use(VueHttp, axios)
Vue.use(CommonUtil)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  // store,
  components: { App },
  template: '<App/>'
})
