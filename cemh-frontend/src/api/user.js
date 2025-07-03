import request from '@/utils/request'

// 用户管理API
export const userApi = {
  // 获取用户列表
  getUserList(params) {
    return request({
      url: '/api/users',
      method: 'get',
      params
    })
  },

  // 根据ID获取用户详情
  getUserById(id) {
    return request({
      url: `/api/users/${id}`,
      method: 'get'
    })
  },

  // 创建用户
  createUser(data) {
    return request({
      url: '/api/users',
      method: 'post',
      data
    })
  },

  // 更新用户
  updateUser(id, data) {
    return request({
      url: `/api/users/${id}`,
      method: 'put',
      data
    })
  },

  // 删除用户
  deleteUser(id) {
    return request({
      url: `/api/users/${id}`,
      method: 'delete'
    })
  },

  // 批量删除用户
  batchDeleteUsers(ids) {
    return request({
      url: '/api/users/batch',
      method: 'delete',
      data: ids
    })
  },

  // 更新用户状态
  updateUserStatus(id, status) {
    return request({
      url: `/api/users/${id}/status`,
      method: 'put',
      params: { status }
    })
  },

  // 重置用户密码
  resetPassword(id) {
    return request({
      url: `/api/users/${id}/password/reset`,
      method: 'put'
    })
  },

  // 修改用户密码
  changePassword(id, oldPassword, newPassword) {
    return request({
      url: `/api/users/${id}/password`,
      method: 'put',
      params: { oldPassword, newPassword }
    })
  },

  // 获取用户角色
  getUserRoles(id) {
    return request({
      url: `/api/users/${id}/roles`,
      method: 'get'
    })
  },

  // 分配用户角色
  assignUserRoles(id, roleIds) {
    return request({
      url: `/api/users/${id}/roles`,
      method: 'put',
      data: roleIds
    })
  },

  // 导出用户列表
  exportUsers(params) {
    return request({
      url: '/api/users/export',
      method: 'get',
      params,
      responseType: 'blob'
    })
  },

  // 获取部门下的用户
  getUsersByDept(deptId) {
    return request({
      url: `/api/users/dept/${deptId}`,
      method: 'get'
    })
  },

  // 更新用户头像
  updateUserAvatar(id, data, config) {
    return request({
      url: `/api/users/${id}/avatar`,
      method: 'put',
      data,
      ...(config || {})
    })
  }
}

export const tenantApi = {
  // 获取租户列表
  getTenantList() {
    return request({
      url: '/api/tenants/all',
      method: 'get'
    })
  }
}

