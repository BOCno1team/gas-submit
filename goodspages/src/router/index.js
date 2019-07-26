import Vue from 'vue'
import Router from 'vue-router'
import indexNew from '@/components/indexNew'
import evaluation from '@/components/evaluation'
import supplyDemand from '@/components/supplyDemand'
import workGroup from '@/components/workGroup'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: 'indexNew'
    },
    {
      path: '/indexNew',
      name: 'indexNew',
      component: indexNew
    },
    {
      path: '/evaluation',
      name: 'evaluation',
      component: evaluation
    },
    {
      path: '/workGroup',
      name: 'workGroup',
      component: workGroup
    },
    {
      path: '/supplyDemand',
      name: 'supplyDemand',
      component: supplyDemand
    }
  ]
})
