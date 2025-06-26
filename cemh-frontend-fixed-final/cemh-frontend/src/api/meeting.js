import request from '@/utils/request'

// 会议管理API
export const meetingApi = {
  // 创建会议
  createMeeting(meetingDTO, tenantId, userId) {
    const headers = {};
    if (tenantId) headers['X-Tenant-Id'] = tenantId;
    if (userId) headers['X-User-Id'] = userId;

    return request({
      url: '/api/meeting/create',
      method: 'post',
      data: meetingDTO,
      headers
    });
  },

  // 更新会议
  updateMeeting(meetingDTO, tenantId, userId) {
    const headers = {};
    if (tenantId) headers['X-Tenant-Id'] = tenantId;
    if (userId) headers['X-User-Id'] = userId;

    return request({
      url: '/api/meeting/update',
      method: 'put',
      data: meetingDTO,
      headers
    });
  },

  // 删除会议
  deleteMeeting(id, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: `/api/meeting/delete/${id}`,
      method: 'delete',
      headers
    });
  },

  // 获取会议详情
  getMeetingDetail(id, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: `/api/meeting/detail/${id}`,
      method: 'get',
      headers
    });
  },

  // 获取会议列表
  getMeetingList(queryDTO, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: '/api/meeting/list',
      method: 'post',
      data: queryDTO,
      headers
    });
  },

  // 取消会议
  cancelMeeting(id, reason, tenantId, userId) {
    const headers = {};
    if (tenantId) headers['X-Tenant-Id'] = tenantId;
    if (userId) headers['X-User-Id'] = userId;

    return request({
      url: `/api/meeting/cancel/${id}`,
      method: 'put',
      params: { reason },
      headers
    });
  },

  // 发布会议
  publishMeeting(id, tenantId, userId) {
    const headers = {};
    if (tenantId) headers['X-Tenant-Id'] = tenantId;
    if (userId) headers['X-User-Id'] = userId;

    return request({
      url: `/api/meeting/publish/${id}`,
      method: 'put',
      headers
    });
  },

  // 置顶会议
  topMeeting(id, isTop, tenantId, userId) {
    const headers = {};
    if (tenantId) headers['X-Tenant-Id'] = tenantId;
    if (userId) headers['X-User-Id'] = userId;

    return request({
      url: `/api/meeting/top/${id}`,
      method: 'put',
      params: { isTop },
      headers
    });
  }
};
