import * as types from './mutation-types'

export default {
  saveLocale({commit}, locale) {
    commit(types.SAVE_LOCALE, locale)
  },
  saveToken({commit}, token) {
    commit(types.SAVE_TOKEN, token)
  },
  saveUser({commit}, payload) {
    commit(types.SAVE_USER, payload.user)
    commit(types.SAVE_TOKEN, payload.token)
  },
  logout({commit}) {
    commit(types.LOGOUT)
  },
  saveWarnForm({commit}, getWarnForm) {
    commit(types.SAVE_WARNFORM, getWarnForm)
  },
  saveFinancialForm({commit}, financialForm) {
    commit(types.SAVE_FINANCIALFORM, financialForm)
  },
  saveInfoQuery({commit}, infoQuery) {
    commit(types.SAVE_INFOQUERY, infoQuery)
  }
}
