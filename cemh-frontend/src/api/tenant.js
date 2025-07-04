import request from '@/utils/request'

// 获取租户列表（分页+条件）
export function getTenantList(params) {
  return request({
    url: '/api/tenants',
    method: 'get',
    params
  })
}

// 新增租户
export function createTenant(data) {
  return request({
    url: '/api/tenants',
    method: 'post',
    data
  })
}

// 修改租户
export function updateTenant(id, data) {
  return request({
    url: `/api/tenants/${id}`,
    method: 'put',
    data
  })
}

// 删除租户
export function deleteTenant(id) {
  return request({
    url: `/api/tenants/${id}`,
    method: 'delete'
  })
}

// 彻底删除租户
export function realDeleteTenant(id) {
  return request({
    url: `/api/tenants/real/${id}`,
    method: 'delete'
  })
}

// 续费租户
export function renewTenant({ tenantId, years, months, days }) {
  return request({
    url: '/api/tenants/renew',
    method: 'post',
    params: { tenantId, years, months, days }
  })
}

// 批量续费租户
export function batchRenewTenants({ tenantIds, years, months, days }) {
  return request({
    url: '/api/tenants/batch-renew',
    method: 'post',
    data: tenantIds,
    params: { years, months, days }
  })
}

// 导出租户数据
export function exportTenants(params) {
  // 过滤掉值为null或undefined的参数
  const filteredParams = {};
  Object.keys(params).forEach(key => {
    if (params[key] !== null && params[key] !== undefined) {
      filteredParams[key] = params[key];
    }
  });
  
  const queryString = new URLSearchParams(filteredParams).toString();
  const url = `http://localhost:8080/api/tenants/export?${queryString}`;
  window.open(url, '_blank');
  return Promise.resolve();
}

// 停用租户
export function disableTenant(tenantId) {
  return request({
    url: '/api/tenants/disable',
    method: 'post',
    params: { tenantId }
  })
}

// 启用租户
export function enableTenant(tenantId) {
  return request({
    url: '/api/tenants/enable',
    method: 'post',
    params: { tenantId }
  })
} 