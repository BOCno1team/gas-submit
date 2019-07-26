import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import * as Cookies from 'js-cookie'
import mutations from './mutations'
import actions from './actions'

Vue.use(Vuex)

const state = {
  locale: 'zh',
  user: {
    id: null,
    name: '',
    gender: '',
    lastlogintime: '',
    avatar: '',
    pwdExpired: true,
    instno: '003',
    branchid: null,
    branchname: '',
    roleid: [],
    rolename: [],
    authority: [],
    state: ''
  },
  token: null,
  getWarnForm: {},
  infoQuery: {},
  financialForm: []
}

export default new Vuex.Store({
  state,
  mutations,
  actions,
  strict: process.env.NODE_ENV !== 'production',
  plugins: [
    createPersistedState({
      getState: (key) => Cookies.getJSON(key),
      setState: (key, stat) => Cookies.set(key, stat)
    })
  ]
})
