// dept.js
import request from '@/utils/request'

// 获取企业列表 - 已实现
export function getTenants() {
  return request({
    url: '/api/tenants/all',
    method: 'get'
  })
}

// 根据企业ID获取部门 - 已实现
export function getDeptsByTenant(tenantId) {
  return request({
    url: `/api/departments/by-tenant/${tenantId}`,
    method: 'get'
  })
}

// 创建部门 - 需实现
export function createDept(data) {
  return request({
    url: '/api/departments',
    method: 'post',
    data
  })
}

// 更新部门 - 需实现
export function updateDept(id, data) {
  return request({
    url: `/api/departments/${id}`,
    method: 'put',
    data
  })
}

// 删除部门 - 需实现
export function deleteDept(id) {
  return request({
    url: `/api/departments/${id}`,
    method: 'delete'
  })
}