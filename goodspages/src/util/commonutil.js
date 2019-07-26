import md5 from 'md5'

var CommonUtil = {}

/**
 * CommonUtil - 获取返回数据
 * @author pangjian
 * @param  {object} res http返回结果
 * @return {object}     返回的JSON
 */
CommonUtil.getResData = function(res) {
  if (CommonUtil.isSuccess(res)) {
    return res.data.result;
  }else{
    // console.log('请求报错了')
  }
}

/**
 * CommonUtil - 获取错误信息
 * @author pangjian
 * @param  {object} res http返回结果
 * @return {object}     错误信息包含code和message
 */
CommonUtil.getErr = function(res) {
  if (!CommonUtil.isSuccess(res)) {
    return {
      code: res.data.code,
      message: res.data.message
    }
  }
}

/**
 * CommonUtil - 检查返回是否成功
 * @author pangjian
 * @param  {object} res http返回结果
 * @return {boolean}    是否成功标识
 */
CommonUtil.isSuccess = function(res) {
  var flag = res.data._isException_;
  return !flag;
}

/**
* CommonUtil - 检查返回是否成功
* @author lfh4659
* @param {object} res http返回结果
* @return {boolean} 是否成功标识
*/
CommonUtil.isOK = function(res) {
  if (!res.data.returnCode) {
    return false;
  }
  if (res.data.returnCode === '000000') {
    return true;
  } else {
    return false;
  }
}

/**
* CommonUtil - 检查返回是否成功
* @author lfh13738
* @param {object} res http返回结果
* @return {boolean} 是否成功标识
*/
CommonUtil.isOK = function(res) {
  if (!res.statusText) {
    return false;
  }
  if (res.statusText === 'OK') {
    return true;
  } else {
    return false;
  }
}
/**
* CommonUtil - 日期格式转换
* @author lfh13738
* @param {date} original_date 日期类型
* @return {string} yyyy-MM-dd hh:mm:ss
*/
CommonUtil.datetimeFormat = function(original_date) {
  if (!original_date || original_date === '') {
    return null;
  }
  let year = original_date.getFullYear();
  let month = original_date.getMonth() + 1;
  month = (month < 10 ? '0' + month : month);
  let date = (original_date.getDate() < 10 ? '0' + original_date.getDate() : original_date.getDate());
  let hour = (original_date.getHours() < 10 ? '0' + original_date.getHours() : original_date.getHours());
  let minute = (original_date.getMinutes() < 10 ? '0' + original_date.getMinutes() : original_date.getMinutes());
  let second = (original_date.getSeconds() < 10 ? '0' + original_date.getSeconds() : original_date.getSeconds());
  let format_date = year + '年' + month + '月' + date + '日' + hour + ':' + minute + ':' + second;
  return format_date;
}

/**
* CommonUtil - 日期格式转换
* @author lfh13738
* @param {date} original_date 日期类型
* @param {date} status 判断是否添加时间单位
* @return {string} yyyyMMdd
*/
CommonUtil.dateFormat = function(original_date, status) {
  if (!original_date || original_date === '') {
    return null;
  }
  let year = original_date.getFullYear();
  let month = original_date.getMonth() + 1;
  month = (month < 10 ? '0' + month : month);
  let date = (original_date.getDate() < 10 ? '0' + original_date.getDate() : original_date.getDate());
  let format_date = '';
  if(status === 'addUnit') {
    format_date = year + '年' + month + '月' + date + '日';
  } else {
    format_date = year + month + date;
  }
  
  return format_date;
}
/**
 * 返回md5对象
*/
CommonUtil.md5 = md5

CommonUtil.install = function(Vue) {
  Object.defineProperties(Vue.prototype, {
    $CU: {
      get() {
        return CommonUtil
      }
    }
  })
}

export default CommonUtil
