<template>
  <div id="app">
    <div class="header">
      <img src="./assets/logo-white.png" @click="goBack"/>
      <div class="headerLeft">
        <span>全球援助撮合系统</span>
        <em>The Global Aid Match System</em>
      </div>
    </div>
    <router-view v-if="isRouterAlive"></router-view>
  </div>
</template>

<script>
// import picUrl from "./assets/logo-white.png"
export default {
  name: 'App',
  provide() {
    return {
      reload: this.reload
    }
  },
  data() {
    return {
      isRouterAlive: true
    }
  },
  methods: {
    goBack() {
      this.$router.push('/indexNew')
    },
    reload() {
      this.isRouterAlive = false
      this.$nextTick(function() {
        this.isRouterAlive = true
      })
    }
  },
}
</script>

<style>
html,body {
  margin: 0;
  padding: 0;
  background: #eee;
}
.header {
  padding:  10px;
  background: #be2e3e;
}
.header img {
  width: 200px;
  height: 50px;
  display: inline-block;
  cursor: pointer;
}
.headerLeft {
  float: right;
  width: 200px;
  height: 60px;
  margin-top: -3px;
}
.header span {
  display: block;
  color: #fff;
  font-size: 20px;
  line-height: 60px;
  margin-top: -10px;
}
.header em {
  width: 218px;
  font-size: 14px;
  color: #fff;
  float: right;
  font-style: normal;
  margin-top: -14px;
}
</style>
