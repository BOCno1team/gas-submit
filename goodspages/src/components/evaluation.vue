<template>
  <div class="evalution-body" v-loading="loading" element-loading-text="Submitting..." element-loading-background="rgba(0, 0, 0, 0.8)">
    <p>请对其他参与方进行评价/Please give feedback to other participants</p>
    <el-tabs class="evalutionStyle" v-model="activeName2" type="card" @tab-click="handleClick" v-if='isWrapperForm'>
      <el-tab-pane label="供给方指标评价/Supplier Indicator Quality" name="first" class="warn-tab-pane" v-if="supplierHidden">
        <el-form :model="supplierForm" ref="supplierForm" label-width="250px" class="demo-ruleForm">
          <div class="row seconds-title">
            <div class="col-md-12 col supplierInfo">
              <el-form-item label="供应信息/Supply Information">
                <button>OrgID：{{supOrgId}}</button>
                <button>OrgName：{{supOrgName}}</button>
                <button>{{supOrgGoods}}</button>
                <button>{{supOrgAmount}}{{supOrgUnit}}</button>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="物资质量/Product Quality">
                <el-rate
                  v-model="supplierForm.materialQuality"
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="供给速度/ Supply Speed">
                <el-rate
                  v-model="supplierForm.supplySpeed"
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="信息准确/ Information Accuracy">
                <el-rate
                  v-model="supplierForm.accurateInfo"
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="按时交付/Ontime Delivery">
                <el-rate
                  v-model="supplierForm.Delivery"
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="服务质量/Service Quality">
                <el-rate
                  v-model="supplierForm.qualityService "
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-12 col">
              <el-form-item class="evalutionCommit">
                <el-button type="primary" @click="submitSupplyForm">提交/Submit</el-button>
              </el-form-item>
            </div>
          </div>
        </el-form>
      </el-tab-pane>
      
      <el-tab-pane label="需求方指标评价/Demander Indicator Evaluation" name="second" v-if="damanderHidden">
        <el-form :model="damanderForm" ref="damanderForm" label-width="250px" class="demo-ruleForm">
          <div class="row seconds-title">
            <div class="col-md-12 col supplierInfo">
              <el-form-item label="需求信息/Demand Information">
                <button>OrgID：{{demOrgId}}</button>
                <button>OrgName：{{demOrgName}}</button>
                <button>{{demOrgGoods}}</button>
                <button>{{demOrgAmount}}{{demOrgUnit}}</button>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="准确验收/Accurate Acceptance">
                <el-rate
                  v-model="damanderForm.checkGoods"
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="实事求是/Honesty ">
                <el-rate
                  v-model="damanderForm.real"
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="沟通态度/Communication Attitude">
                <el-rate
                  v-model="damanderForm.communicate"
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="诚信守约/Integrity">
                <el-rate
                  v-model="damanderForm.sincerity "
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="待定/TBA">
                <el-rate
                  v-model="damanderForm.daiDing"
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-12 col">
              <el-form-item class="evalutionCommit">
                <el-button type="primary" @click="submitDamanderForm">提交/Submit</el-button>
              </el-form-item>
            </div>
          </div>
        </el-form>
      </el-tab-pane>

      <el-tab-pane label="实施方指标评价/Executor Indicator Evaluation" name="three" v-if="implementationHidden">
        <el-form :model="implementationForm" ref="implementationForm" label-width="250px" class="demo-ruleForm">
          <div class="row seconds-title">
            <div class="col-md-12 col supplierInfo">
              <el-form-item label="实施信息/Execution Information">
                <button>OrgID：{{supOrgId}}</button>
                <button>OrgName：{{supOrgName}}</button>
                <button>{{supOrgGoods}}</button>
                <button>{{supOrgAmount}}{{supOrgUnit}}</button>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="实施质量/Implementation Quality">
                <el-rate
                  v-model="implementationForm.QualityImple"
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="服务态度/Service Attitude">
                <el-rate
                  v-model="implementationForm.serviceAttitude"
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="诚信守时/Honesty&Punctuality">
                <el-rate
                  v-model="implementationForm.honestyPunct"
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="物尽其用/Useage">
                <el-rate
                  v-model="implementationForm.appropriate"
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="实施效率/Implementation Efficiency">
                <el-rate
                  v-model="implementationForm.efficiency"
                  :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="10">
                </el-rate>
              </el-form-item>
            </div>
            <div class="col-md-12 col">
              <el-form-item class="evalutionCommit">
                <el-button type="primary" @click="submitImpleForm">提交/Submit</el-button>
              </el-form-item>
            </div>
          </div>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
  </template>

<script>
export default {
  inject: ['reload'],
  data() {
    return {
      loading: false,
      isWrapperForm: true,
      activeName2: 'first',
      supplierHidden: true,
      damanderHidden: true,
      implementationHidden: true,
      supplierForm: { 
        materialQuality: null, 
        supplySpeed: null, 
        accurateInfo: null, 
        Delivery: null, 
        qualityService: null 
      }, 
      supOrgId: '',
      supOrgName: '',
      supOrgGoods: '',
      supOrgUnit: '',
      supOrgAmount: '',
      demOrgId: '',
      demOrgName: '',
      demOrgGoods: '',
      demOrgUnit: '',
      demOrgAmount: '',
      demplierForm: {
        materialQuality: null,
        supplySpeed: null,
        accurateInfo: null,
        Delivery: null,
        qualityService: null
      },
      damanderForm: {
        checkGoods: null,
        real: null,
        communicate: null,
        sincerity: null,
        daiDing: null
      },
      implementationForm: {
        QualityImple: null,
        serviceAttitude: null,
        honestyPunct: null,
        appropriate: null,
        efficiency: null
      }
    }
  },
  beforeMount() {
    const demandID = JSON.parse(this.$route.query.DemandID)
    this.axios.post('/awp/CommentServlet', {demandId: demandID})
    .then(res => {
      if (this.$CU.isOK(res)) {
        // Verify the rule of current participant before render
        res.data.resultArray.map(item => {
          if(item.orgType === 'demander') {
            this.damanderHidden = false
          }else if(item.orgType === 'provider') {
            this.supplierHidden = false
          }
          this.supOrgId = item.orgId
          this.supOrgName = item.orgName
          this.supOrgGoods = item.supplyName
          this.supOrgUnit = item.unit
          this.supOrgAmount = item.supplyAmount
          this.demOrgId = item.orgId
          this.demOrgName = item.orgName
          this.demOrgGoods = item.demandName
          this.demOrgUnit = item.unit
          this.demOrgAmount = item.demandAmount
        })
      } else {
        this.loading = false
        this.$alert('暂无数据', '提示', {
          confirmButtonText: '确定',
          type: 'info'
        });
      }
    })
    .catch(err => {
      this.loading = false
      this.$alert('请求失败! ' + err, '提示', {
        confirmButtonText: '确定',
        type: 'error'
      });
    })
  },
  methods: {
    // Due to server expiration, we couldn't debug
    handleClick() {},
    submitSupplyForm(){
      //Submit supplier score
      this.$refs.supplierForm.validate((valid) => {
        if (valid) {
          this.loading = true
          this.axios.post('/awp/CommentServlet', this.supplierForm)
          .then(res => {
            if (this.$CU.isOK(res)) {
              this.loading = false
              this.$alert('提交成功', '提示', {
                confirmButtonText: '确定',
                type: 'success'
              });
              this.reload()
            } else {
              this.loading = false
              this.$alert('暂无数据', '提示', {
                confirmButtonText: '确定',
                type: 'info'
              });
            }
          })
          .catch(err => {
            this.loading = false
            this.$alert('请求失败! ' + err, '提示', {
              confirmButtonText: '确定',
              type: 'error'
            });
          })
        } else {
          return false;
        }
      });
    },
    submitDamanderForm() {
      //Submit demander score
      this.$refs.damanderForm.validate((valid) => {
        if (valid) {
          this.loading = true
          this.axios.post('/awp/CommentServlet', this.damanderForm)
          .then(res => {
            if (this.$CU.isOK(res)) {
              this.loading = false
              this.$alert('提交成功', '提示', {
                confirmButtonText: '确定',
                type: 'success'
              });
              this.reload()
            } else {
              this.loading = false
              this.$alert('暂无数据', '提示', {
                confirmButtonText: '确定',
                type: 'info'
              });
            }
          })
          .catch(err => {
            this.loading = false
            this.$alert('请求失败! ' + err, '提示', {
              confirmButtonText: '确定',
              type: 'error'
            });
          })
        } else {
          return false;
        }
      });
    },
    submitImpleForm() {
      //Submit executor score
      this.$refs.implementationForm.validate((valid) => {
        if (valid) {
          this.loading = true
          this.axios.post('/awp/CommentServlet', this.implementationForm)
          .then(res => {
            if (this.$CU.isOK(res)) {
              this.loading = false
              this.$alert('提交成功', '提示', {
                confirmButtonText: '确定',
                type: 'success'
              });
              this.reload()
            } else {
              this.loading = false
              this.$alert('暂无数据', '提示', {
                confirmButtonText: '确定',
                type: 'info'
              });
            }
          })
          .catch(err => {
            this.loading = false
            this.$alert('请求失败! ' + err, '提示', {
              confirmButtonText: '确定',
              type: 'error'
            });
          })
        } else {
          return false;
        }
      });
    }
  }
}
</script>

<style>
  .evalution-body {
    overflow: hidden;
    min-height: 680px;
  }
  .evalution-body p {
    text-align: center;
    font-weight: 900;
    color: #be2e3e;
    font-size: 18px;
  }
  .el-breadcrumb {
    font-size: 14px;
    line-height: 14px;
    font-weight: bold;
    margin-top: 10px;
    margin-bottom: 20px;
  }
  .evalutionStyle {
    margin: 30px auto;
    border: 0;
  }
  .evalutionStyle>.el-tabs__header {
    border-bottom: 0;
    margin: 0 0 50px 0;
  }
  .supplierInfo {
    width: 600px;
  }
  .supplierInfo .el-form-item__label {
    color: #be2e3e;
  }
  .supplierInfo .el-form-item__content {
    width: 350px;
  }
  .supplierInfo button {
    border: 0;
    outline: none;
    /* background: #ccc; */
    margin-right: 10px;
    border-radius: 3px;
    width: 155px;
    height: 30px;
    color: #be2e3e;
  }
  .evalutionStyle>.el-tabs__header>.el-tabs__nav-wrap>.el-tabs__nav-scroll {
    display: flex;
    justify-content: center;
  }
  .evalutionStyle .el-tabs__header .el-tabs__nav-wrap .el-tabs__nav-scroll .el-tabs__nav .el-tabs__item {
    margin-right: 5px;
    color: #999;
    padding: 0 8px;
  }
  .evalutionStyle .el-tabs__header .el-tabs__nav-wrap .el-tabs__nav-scroll .el-tabs__nav .el-tabs__item:hover {
    opacity: .8;
    background-color: #be2e3e;
    border-radius: 5px;
  }
  .evalutionStyle .el-tabs__header .el-tabs__nav-wrap .el-tabs__nav-scroll .el-tabs__nav .el-tabs__item.is-active {
    color: #fff;
    background-color: #be2e3e;
    border-radius: 5px;
  }
  
  .warn-tab-pane {
    line-height: 60px;
    font-size: 25px;
  }
  
  .seconds-title {
    width: 500px;
    margin: auto;
  }
  .seconds-title .el-form-item__label {
    width: 250px !important;
  }
  .seconds-title .el-form-item__content {
    margin-left: 260px !important;
  }
  .el-form-item {
    margin-right: 20px;
  }
  .evalutionCommit {
    margin-top: 30px;
  }
  .evalutionCommit .el-form-item__content {
    margin-left: 430px !important;
  }
  .evalutionCommit .el-button {
    background: #be2e3e;
    color: #fff;
    border: 0;
    outline: none;
  }
  .evalutionCommit .el-button:hover {
    background: #be2e3e;
    opacity: .8;
  }
  .evalutionCommit .el-button:active {
    background: #be2e3e;
    opacity: .8;
  }
  .el-rate {
    width: 240px;
    line-height: 2.5;
  }
</style>