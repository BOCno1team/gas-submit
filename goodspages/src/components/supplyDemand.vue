<template>
  <div class="wrapper-body">
    <el-tabs class="supplyDemandStyle">
      <el-form label-width="195px" class="demo-ruleForm">
        <div class="row supplyDemand-title">
          <div class="col-md-4 col">
            <el-form-item label="需求方本人ID/Demander ID">
              <el-button class="elBtn">{{demander}}</el-button>
            </el-form-item>
          </div>
          <div class="col-md-12 col goodsTittle">
            <h4>无偿物品/Unprofitable Supply</h4>
          </div>
          <div v-for="(item,index) in unprofitableSupplyList" :key="index">
            <div class="col-md-12 col">
              <div class="col-md-4 col">
                <el-form-item label="供给方ID/Supplier ID" prop="provider">
                <el-button class="elBtn">{{item.providerId}}</el-button>
              </el-form-item>
              </div>
              <div class="col-md-4 col">
                <el-form-item label="供给方名称/Supplier Name" prop="supplyName">
                  <el-button class="elBtn">{{item.providerName}}</el-button>
              </el-form-item>
              </div>
              <div class="col-md-4 col">
                <el-form-item label="供给物品ID/Supply ID" :prop="'supplyFormList.'+index+'.supplyId'">
                  <el-button class="elBtn">{{item.supplyId}}</el-button>
                </el-form-item>
              </div>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="物资名称/Supply Name">
                <el-button class="elBtn">{{item.name}}</el-button>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="物资单位/Unit">
                <el-button class="elBtn">{{item.unit}}</el-button>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="物资数量/Amount">
                <el-button class="elBtn">{{item.amount}}</el-button>
              </el-form-item>
            </div>
          </div>
          <div class="col-md-12 col goodsTittle">
            <h4>有偿物品/Profitable Supply</h4>
          </div>
          <div v-for="(item,index) in profitableSupplyList" :key="index">
            <div class="col-md-12 col">
              <div class="col-md-4 col">
                <el-form-item label="供给方ID/Supplier ID" prop="provider">
                <el-button class="elBtn">{{item.providerId}}</el-button>
              </el-form-item>
              </div>
              <div class="col-md-4 col">
                <el-form-item label="供给方名称/Supplier Name" prop="supplyName">
                  <el-button class="elBtn">{{item.providerName}}</el-button>
              </el-form-item>
              </div>
              <div class="col-md-4 col">
                <el-form-item label="供给物品ID/Supply ID" :prop="'supplyFormList.'+index+'.supplyId'">
                  <el-button class="elBtn">{{item.supplyId}}</el-button>
                </el-form-item>
              </div>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="物资名称/Supply Name">
                <el-button class="elBtn">{{item.name}}</el-button>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="物资单位/Unit">
                <el-button class="elBtn">{{item.unit}}</el-button>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="物资数量/Amount">
                <el-button class="elBtn">{{item.amount}}</el-button>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="单位价格/Unit Price">
                <el-button class="elBtn">{{item.unitPrice}}</el-button>
              </el-form-item>
            </div>
          </div>
          <div class="col-md-12 col goodsTittle">
            <h4>资金/Fund</h4>
          </div>
          <div v-for="(item,index) in fundList" :key="index">
            <div class="col-md-12 col">
              <div class="col-md-4 col">
                <el-form-item label="供给方ID/Supplier ID" prop="provider">
                <el-button class="elBtn">{{item.providerId}}</el-button>
              </el-form-item>
              </div>
              <div class="col-md-4 col">
                <el-form-item label="供给方名称/Supplier Name" prop="supplyName">
                  <el-button class="elBtn">{{item.providerName}}</el-button>
              </el-form-item>
              </div>
              <div class="col-md-4 col">
                <el-form-item label="供给物品ID/Supply ID" :prop="'supplyFormList.'+index+'.supplyId'">
                  <el-button class="elBtn">{{item.supplyId}}</el-button>
                </el-form-item>
              </div>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="物资名称/Fund">
                <el-button class="elBtn">{{item.name}}</el-button>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="物资单位/Unit">
                <el-button class="elBtn">{{item.unit}}</el-button>
              </el-form-item>
            </div>
            <div class="col-md-4 col">
              <el-form-item label="金额/Amount">
                <el-button class="elBtn">{{item.amount}}</el-button>
              </el-form-item>
            </div>
          </div>
          <div class="col-md-12 col">
            <el-form-item class="supplyDemandCommit">
              <el-button type="primary" @click="supplyDemand" class="supplyDemandCommitBtn">Confirm & Proceed to Work Group</el-button>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </el-tabs>
  </div>
</template>

<script>
export default {
  data() {
    return {
    demander: '',
    profitableSupplyList: [],
    unprofitableSupplyList: [],
    fundList: [],
    demandGoods: []
    }
  },
  beforeMount() {
    //send request before render
    const demanderData = JSON.parse(this.$route.query.supplyDemand)
    demanderData.DemandList.map(item => {
      this.demandGoods = item.demandId
    })
    this.demander = demanderData.DemanderId
    this.profitableSupplyList = demanderData.ProfitableSupplyList
    this.unprofitableSupplyList = demanderData.UnprofitableSupplyList
    this.fundList = demanderData.fundList
  },
  methods: {
    supplyDemand() {
      //Click the button to jump to another page and send attributes
      this.$router.push({path: "/workGroup", query: {
        DemandID: JSON.stringify(this.demandGoods)
      }})
    }
  }
}
</script>

<style>
  .supplyDemandStyle {
    margin: 25px auto;
    border: 0;
  }
  .goodsTittle {
    font-size: 18px;
    color: #999;
    width: 120%;
    height: 25px;
    line-height: 20px;
    font-weight: 500;
    border-bottom: 1px solid #ccc;
    margin-bottom: 20px;
  }
  .supplyDemand-title {
    width: 620px;
    margin: auto;
  }
  .el-form-item {
    margin-right: 20px;
  }
  .supplyDemandCommit {
    margin-top: 30px;
  }
  .supplyDemandCommitBtn {
    margin-left: 160px;
    background: #be2e3e;
    color: #fff;
    border: 0;
    outline: none;
  } 
  .supplyDemandCommitBtn:hover {
    background: #be2e3e;
    opacity: .8;
  }
  .supplyDemandCommitBtn:active {
    background: #be2e3e;
    opacity: .8;
  }
  .elBtn {
    width: 530px;
    height: 30px;
    text-align: left;
    line-height: 0px;
    font-size: 16px;
    cursor: inherit;
    outline: none;
    border: 0;
  }
</style>