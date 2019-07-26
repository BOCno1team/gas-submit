<template>
  <div class="wrapper-body" v-loading="loading" element-loading-text="拼命提交中..." element-loading-background="rgba(0, 0, 0, 0.8)">
    <el-tabs class="warnTabs" v-model="activeName2" type="card" @tab-click="handleClick" v-if='isWrapperForm'>
        <el-tab-pane label="供给物品/Supply" name="first" class="warn-tab-pane">
          <el-form :model="supplyForm" ref="supplyForm" label-width="200px" class="demo-ruleForm">
            <div class="row supply-title" size="medium">
              <div class="col-md-12 col">
                <el-form-item label="供给方名称/Supplier Name" prop="supplyName">
                  <el-input v-model="supplyForm.supplyName"></el-input>
                </el-form-item>
              </div>
              <div class="col-md-12 col">
                <el-form-item label="供给方ID/Supplier ID" prop="provider">
                  <el-input v-model="supplyForm.provider"></el-input>
                </el-form-item>
              </div>
              <el-row v-for="(item,index) in supplyForm.supplyFormList" :key="index">
                <div class="col-md-12 col">
                  <el-form-item label="供给物品ID/Supply ID" :prop="'supplyFormList.'+index+'.supplyId'">
                    <el-input v-model="item.supplyId"></el-input>
                  </el-form-item>
                </div>
                <div class="col-md-12 col">
                  <el-form-item label="供给物品名称/Supply Name" :prop="'supplyFormList.'+index+'.name'">
                    <el-input v-model="item.name"></el-input>
                  </el-form-item>
                </div>
                <div class="col-md-12 col">
                  <el-form-item label="数量/Amount" :prop="'supplyFormList.'+index+'.amount'">
                    <el-input v-model="item.amount"></el-input>
                  </el-form-item>
                </div>
                <div class="col-md-12 col">
                  <el-form-item label="单位/Unit" :prop="'supplyFormList.'+index+'.unit'">
                    <el-input v-model="item.unit"></el-input>
                  </el-form-item>
                </div>
                <div class="col-md-12 col">
                  <el-form-item label="单位价格/Unit Price" :prop="'supplyFormList.'+index+'.unitPrice'">
                    <el-input v-model="item.unitPrice"></el-input>
                  </el-form-item>
                </div>
                <div class="col-md-12 col" style="height:1px; background-color:#ccc;margin-bottom:20px"></div>
              </el-row>
              <el-row>
                <div class="col-md-12 col">
                  <el-form-item class="commit">
                    <el-button @click="addSupplyForm" class="el-button--primary">增加物品清单/Add more</el-button>
                    <el-button @click="deleteSupplyForm" class="el-button--primary">删除物品清单/Delete</el-button>
                    <el-button type="primary" @click="submitSupplyForm">提交/Submit</el-button>
                    <el-button @click="resetSupplyForm" class="el-button--primary">重置/Reset</el-button>
                  </el-form-item>
                </div>
              </el-row>
            </div>
          </el-form>
        </el-tab-pane>
        <!-- 需求方信息页面  -->
        <el-tab-pane label="需求物品/Demand" name="second">
          <el-form :model="demandForm" ref="demandForm" label-width="230px" class="demo-ruleForm">
            <div class="row supply-title">
              <div class="col-md-4 col">
                <el-form-item label="需求方ID/Demander ID" prop="DemanderId">
                  <el-input v-model="demandForm.DemanderId"></el-input>
                </el-form-item>
              </div>
              <el-row v-for="(item,index) in demandForm.demandFormList" :key="index">
                <div class="col-md-12 col">
                  <el-form-item label="本次需求ID/Demand ID" :prop="'demandFormList.'+index+'.demandId'">
                    <el-input v-model="item.demandId"></el-input>
                  </el-form-item>
                </div>
                <div class="col-md-12 col">
                  <el-form-item label="需求物品名称/Demand Name" :prop="'demandFormList.'+index+'.name'">
                    <el-input v-model="item.name"></el-input>
                  </el-form-item>
                </div>
                <div class="col-md-12 col">
                  <el-form-item label="需求物品种类/Demand Category" :prop="'demandFormList.'+index+'.category'">
                    <el-input v-model="item.category"></el-input>
                  </el-form-item>
                </div>
                <div class="col-md-12 col">
                  <el-form-item label="数量/Amount" :prop="'demandFormList.'+index+'.amount'">
                    <el-input v-model="item.amount"></el-input>
                  </el-form-item>
                </div>
                <div class="col-md-12 col">
                  <el-form-item label="单位/Unit" :prop="'demandFormList.'+index+'.unit'">
                    <el-input v-model="item.unit"></el-input>
                  </el-form-item>
                </div>
                <div class="col-md-12 col" style="height:1px; background-color:#ccc;margin-bottom:20px"></div>
              </el-row>
              <el-row>
                <div class="col-md-12 col">
                  <el-form-item class="commit">
                    <el-button @click="addDemandForm" class="el-button--primary">增加物品清单/Add more</el-button>
                    <el-button @click="deleteDemandForm(item,index)" class="el-button--primary">删除物品清单/Delete</el-button>
                    <el-button type="primary" @click="submitDemandForm">提交/Submit</el-button>
                    <el-button @click="resetDemandForm" class="el-button--primary">重置/Reset</el-button>
                  </el-form-item>
                </div>
              </el-row>
            </div>
          </el-form>
        </el-tab-pane>
      </el-tabs>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      isWrapperForm: true,
      activeName2: 'first',
      elDom: true,
      supplyForm: {
        supplyName: '',
        provider: '',
        supplyFormList: [{
          name: '',
          supplyId: '',
          amount: '',
          unit: '',
          unitPrice: ''
        }]
      },
      demandForm: {
        DemanderId: '',
        demandFormList: [{
          name: '',
          category: '',
          demandId: '',
          amount: '',
          unit: ''
        }]
      }
    }
  },
  methods: {
    handleClick() {},
    addSupplyForm() {
      this.supplyForm.supplyFormList.push({
        name: '',
        supplyId: '',
        amount: '',
        unit: '',
        unitPrice: ''
      })
    },
    submitSupplyForm() {
      this.$refs.supplyForm.validate((valid) => {
        if (valid) {
          //send supply message
          this.loading = true
          let newProvider = {
            supplyName: this.supplyForm.supplyName,
            provider: this.supplyForm.provider,
            supplyFormList: []
          }
          this.supplyForm.supplyFormList.map(item => {
            newProvider.supplyFormList.push(item)
          })
          this.axios.post('/awp/HelloServlet', newProvider)
          .then(res => {
            if (this.$CU.isOK(res)) {
              //receive feedback
              this.loading = false
              this.$alert('提交成功', '提示', {
                confirmButtonText: '确定',
                type: 'success'
              });
              this.$refs.supplyForm.resetFields();
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
    resetSupplyForm() {
      // reset the supply form
      this.$refs.supplyForm.resetFields();
    },
    deleteSupplyForm() {
      //delete the redundant form
      this.supplyForm.supplyFormList.pop({
        name: '',
        supplyId: '',
        amount: '',
        unit: '',
        unitPrice: ''
      })
    },
    addDemandForm() {
      // add a demand form
      this.demandForm.demandFormList.push({
        name: '',
        category: '',
        demandId: '',
        amount: '',
        unit: ''
      })
    },
    submitDemandForm() {
      // submit the demand form
      this.$refs.demandForm.validate((valid) => {
        if (valid) {
          this.loading = true
          let newDemand = {
            DemanderId: this.demandForm.DemanderId,
            demandFormList: []
          }
          this.demandForm.demandFormList.map(item => {
            newDemand.demandFormList.push(item)
          })
          this.axios.post('/awp/DemandServlet', newDemand)
          .then(res => {
            if (this.$CU.isOK(res)) {
              this.loading = false
              this.$alert('提交成功', '提示', {
                confirmButtonText: '确定',
                type: 'success'
              });
              this.$router.push({path: "/supplyDemand", query: {
                supplyDemand: JSON.stringify(res.data)
              }})
              this.$refs.demandForm.resetFields();
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
    resetDemandForm() {
      // reset the demand form
      this.$refs.demandForm.resetFields();
    },
    deleteDemandForm(item,index) {
      //delete the redundant form
      this.demandForm.demandFormList.pop({
        name: '',
        category: '',
        demandId: '',
        amount: '',
        unit: ''
      })
    }
  }
}
</script>

<style>
  .wrapper-body {
    overflow: hidden;
    min-height: 680px;
  }
  
  .el-breadcrumb {
    font-size: 14px;
    line-height: 14px;
    font-weight: bold;
    margin-top: 10px;
    margin-bottom: 20px;
  }
  .warnTabs {
    margin-top: 25px;
    border: 0;
  }
  .warnTabs>.el-tabs__header {
    border-bottom: 0;
    margin: 0 0 50px 0;
  }
  
  .warnTabs>.el-tabs__header>.el-tabs__nav-wrap>.el-tabs__nav-scroll {
    display: flex;
    justify-content: center;
  }
  
  .warnTabs>.el-tabs__header>.el-tabs__nav-wrap>.el-tabs__nav-scroll>.el-tabs__nav>.el-tabs__item {
    padding: 0 40px;
    color: #999;
    margin-right: 5px;
  }
  .warnTabs>.el-tabs__header>.el-tabs__nav-wrap>.el-tabs__nav-scroll>.el-tabs__nav>.el-tabs__item:hover {
    opacity: .8;
    background-color: #be2e3e;
    border-radius: 5px;
  }
  .warnTabs>.el-tabs__header>.el-tabs__nav-wrap>.el-tabs__nav-scroll>.el-tabs__nav>.el-tabs__item.is-active {
    color: #fff;
    background-color: #be2e3e;
    border-radius: 5px;
  }
  
  .warn-tab-pane {
    line-height: 60px;
    font-size: 25px;
  }
  .supply-title {
    width: 850px;
    margin: auto;
  }
  .el-form-item {
    margin-right: 20px;
  }
  .el-input__inner:focus {
    border: 1px solid #be2e3e;
  }
  .el-select {
    width: 100%;
  }
  .el-select .el-input__inner:focus {
    border: 1px solid #be2e3e;
  }
  .el-select .el-input.is-focus .el-input__inner {
    border: 1px solid #be2e3e;
  }
  .commit {
    margin-top: 30px;
    float: left;
  }
  .commit .el-button {
    background: #be2e3e;
    color: #fff;
    border: 0;
    outline: none;
  }
  .commit .el-button:hover {
    background: #be2e3e;
    opacity: .8;
  }
  .commit .el-button:active {
    background: #be2e3e;
    opacity: .8;
  }
  .commit .el-form-item__content {
    margin-left: 200px !important;
  }
</style>
