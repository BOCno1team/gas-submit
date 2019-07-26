<template>
	<div class="workGroup">
		<p>说明: 请发布最新救援工作状态/Instructions: Please release the latest rescue status</p>
		<el-tabs v-model="editableTabsValue" type="card">
			<el-tab-pane v-for="(item, index) in editableTabs" :key="index" :label="item.name + - + item.id">
				{{item.content}}
			</el-tab-pane>
		</el-tabs>
		<div style="margin-bottom: 20px;">
			<el-input
				type="textarea"
				autosize
				v-model="publisherInfo">
			</el-input>
			<el-button size="small"	@click="addInfo" class="addInfoBtn">
					发布
			</el-button>
		</div>
    <el-form ref="workGroupForm" :model="workGroupForm" label-width="300px">
			<el-form-item label="供应方/Provider" class="supplyItem">
				<el-button  class="ipt">{{supplyUserInfoName + '-' + supplyUserInfoId}}</el-button>
				<el-checkbox name="supplyType" v-model="checked1" :disabled="disabledSupply"></el-checkbox>
			</el-form-item>
			<el-form-item label="需求方/Demander" class="demandItem">
				<el-button  class="ipt">{{demandUserInfoName + '-' + demandUserInfoId}}</el-button>
				<el-checkbox name="demandItemType" v-model="checked2" :disabled="disabledDemand"></el-checkbox>
			</el-form-item>
			<el-form-item label="实施方/Executor" class="demandItem">
				<el-button class="ipt">{{executorUserInfoName + '-' + executorUserInfoId}}</el-button>
				<el-checkbox name="implementType" v-model="checked3" :disabled="disabledImplement"></el-checkbox>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="onComment" class="addInfoBtn">评价/Comment</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>
<script>
export default {
	data() {
		return {
			editableTabsValue: '2',
			editableTabs: [{
				id: '',
				name: '',
				content: ''
			}],
			publisherInfo: '',
			tabIndex: '',
			demandId: '',
			checked1: true,
			disabledSupply: true,
			checked2: false,
			disabledDemand: false,
			checked3: true,
			disabledImplement: true,
			workGroupForm: {
				supplierUser: '',
				supplyType: [],
				demandItemType: [],
				implementType: []
			},
			demandUserInfoName: '',
			demandUserInfoId: '',
			supplyUserInfoName: '',
			supplyUserInfoId: '',
			executorUserInfoName: '',
			executorUserInfoId: ''
		}
	},
	mounted() {
		//Fetch the data and transfer to JSON format after render
		this.demandId = JSON.parse(this.$route.query.DemandID)
  },
	methods: {
		addInfo() {
			//publish participant information
			let userInfo = {
				goodsId: this.demandId,
				content: this.publisherInfo
			}
			if(this.publisherInfo !== null) {
				//request to post information to back end
				this.axios.post('/awp/WorkingMessageServlet', userInfo)
				.then(res => {
					if (this.$CU.isOK(res)) {
						res.data.orgList.forEach(item => {
							if (item.orgType === 'demander') {
								this.demandUserInfoName = item.orgName
								this.demandUserInfoId = item.orgId
							}else if(item.orgType === 'provider') {
								this.supplyUserInfoName = item.orgName
							  this.supplyUserInfoId = item.orgId
							}else {
								this.executorUserInfoName = item.orgName
							  this.executorUserInfoId = item.orgId
							}

						})
						let newTabName = ++this.tabIndex + '';
						this.editableTabs.push({
							id: this.demandUserInfoId,
							name: this.demandUserInfoName,
							content: this.publisherInfo
						});
						this.editableTabsValue = newTabName;
						this.publisherInfo = null
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
			}else {
				this.$alert('请输入内容', '提示', {
					confirmButtonText: '确定',
					type: 'info'
				});
			}
		},
		onComment() {
			//Verify all the factors are true before jumping to other pages
      if (this.checked1 === true && this.checked2 === true && this.checked3 === true) {
				this.$router.push({path: '/evaluation', query: {
        DemandID: JSON.stringify(this.demandId)
      }})
			}else if(this.checked1 === false || this.checked2 === false || this.checked3 === false) {
        this.$alert('请确认是否已签字并选中', '提示', {
					confirmButtonText: '确定',
					type: 'info'
				});
			}
		}
	}
}
</script>
<style>
.workGroup{
	width: 750px;
	margin: auto;
}
.workGroup p{
	color: #be2e3e;
	font-size: 18px;
	font-weight: 600;
	text-align: center;
	margin: 20px 0;
}
.workGroup .el-tabs {
	width: 750px;
	margin: auto;
	margin-bottom: 20px;
	min-height: 380px;
	overflow-y: scroll;
	border: 1px solid #c5c5c5;
}
.workGroup .el-tabs>.el-tabs__header {
	width: 260px;
	float: left;
}
.workGroup .el-tabs>.el-tabs__header .el-tabs__item {
	display: block;
	color: #be2e3e;
}
.workGroup .el-textarea {
	width: 680px;
}
.workGroup .el-textarea>.el-textarea__inner {
	min-height: 39px !important;
	height: 39px !important;
}
.workGroup .el-textarea>.el-textarea__inner:focus {
	border: 1px solid #be2e3e !important;
}
.workGroup .el-form-item .el-form-item__content {
	margin-left: 50px !important;
}
.workGroup .addInfoBtn {
	float: right;
	background: #be2e3e;
	border: 0;
	color: #fff;
	padding: 12px 15px;
	font-size: 14px;
}
.workGroup .el-button:hover {
	opacity: .8;
}
.workGroup .el-button:active {
	opacity: .8;
}
#tab-0 {
	display: none;
}
.ipt {
	width: 200px;
	float: left;
	margin-right: 20px;
	margin-top: 5px;
	height: 35px;
}
.supplyItem {
	float: left;
}
.demandItem {
	float: left;
}
.supplyItem .el-form-item__label {
	width: 60px !important;
}
.supplyItem .el-form-item__content {
	width: 250px !important;
}
.demandItem .el-form-item__label {
	width: 60px !important;
}
.demandItem .el-form-item__content {
	width: 250px !important;
}
</style>

