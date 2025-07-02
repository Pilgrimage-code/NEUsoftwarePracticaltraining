import request from '../utils/request'

export const courseApi = {
  // 获取课程列表
  getCourses(params, tenantId) {
    const headers = {}
    if (tenantId) headers['X-Tenant-Id'] = tenantId

    return request({
      url: '/api/courses',
      method: 'get',
      params,
      headers
    })
  },

  // 获取课程列表（别名，与CourseManagement.vue中的调用保持一致）
  getCourseList(params, tenantId) {
    return this.getCourses(params, tenantId)
  },

  // 获取待审核课程列表
  getPendingReviewCourses(params, tenantId) {
    const headers = {}
    if (tenantId) headers['X-Tenant-Id'] = tenantId

    return request({
      url: '/api/courses/pending-review',
      method: 'get',
      params,
      headers
    })
  },

  // 审核课程
  reviewCourse(id, reviewData, tenantId, userId) {
    const headers = {}
    if (tenantId) headers['X-Tenant-Id'] = tenantId
    if (userId) headers['X-User-Id'] = userId

    return request({
      url: `/api/courses/${id}/review`,
      method: 'put',
      params: {
        status: reviewData.status,
        reviewComment: reviewData.reviewComment
      },
      headers
    })
  },

  // 获取课程详情
  getCourseById(id, tenantId) {
    const headers = {}
    if (tenantId) headers['X-Tenant-Id'] = tenantId

    return request({
      url: `/api/courses/${id}`,
      method: 'get',
      headers
    })
  },

  // 创建课程
  createCourse(data, tenantId, userId) {
    const headers = {}
    if (tenantId) headers['X-Tenant-Id'] = tenantId
    if (userId) headers['X-User-Id'] = userId

    return request({
      url: '/api/courses',
      method: 'post',
      data,
      headers
    })
  },

  // 更新课程
  updateCourse(id, data, tenantId, userId) {
    const headers = {}
    if (tenantId) headers['X-Tenant-Id'] = tenantId
    if (userId) headers['X-User-Id'] = userId

    return request({
      url: `/api/courses/${id}`,
      method: 'put',
      data,
      headers
    })
  },

  // 删除课程
  deleteCourse(id, tenantId) {
    const headers = {}
    if (tenantId) headers['X-Tenant-Id'] = tenantId

    return request({
      url: `/api/courses/${id}`,
      method: 'delete',
      headers
    })
  },

  // 批量删除课程
  batchDeleteCourse(ids, tenantId) {
    const headers = {}
    if (tenantId) headers['X-Tenant-Id'] = tenantId

    return request({
      url: '/api/courses/batch',
      method: 'delete',
      data: ids,
      headers
    })
  },

  // 获取课程分类
  getCourseCategories(tenantId) {
    const headers = {}
    if (tenantId) headers['X-Tenant-Id'] = tenantId

    return request({
      url: '/api/courses/categories',
      method: 'get',
      headers
    })
  },

  // 获取热门课程
  getHotCourses(limit = 10) {
    return request({
      url: '/api/courses/hot',
      method: 'get',
      params: { limit }
    })
  },

  // 获取最新课程
  getLatestCourses(limit = 10) {
    return request({
      url: '/api/courses/latest',
      method: 'get',
      params: { limit }
    })
  },

  // 增加课程浏览次数
  incrementViewCount(id) {
    return request({
      url: `/api/courses/${id}/view`,
      method: 'put'
    })
  },

  // 导出课程列表
  exportCourseList(params, tenantId) {
    const headers = {}
    if (tenantId) headers['X-Tenant-Id'] = tenantId

    return request({
      url: '/api/courses/export',
      method: 'get',
      params,
      headers,
      responseType: 'blob'
    })
  },

  // 导出课程数据
  exportCourses(params) {
    return request({
      url: '/api/courses/export',
      method: 'get',
      params: params,
      responseType: 'blob'
    })
  },

  // 获取课程章节列表
  getCourseChapters(courseId) {
    return request({
      url: `/api/courses/${courseId}/chapters`,
      method: 'get'
    })
  },

  // 获取章节详情
  getChapterDetail(courseId, chapterId) {
    return request({
      url: `/api/courses/${courseId}/chapter/${chapterId}`,
      method: 'get'
    })
  },

  // 创建章节
  createChapter(courseId, data) {
    // 确保使用正确的URL路径
    return request({
      url: `/api/courses/chapter`,
      method: 'post',
      data
    })
  },

  // 更新章节
  updateChapter(courseId, chapterId, data) {
    return request({
      url: `/api/courses/${courseId}/chapter/${chapterId}`,
      method: 'put',
      data
    })
  },

  // 删除章节
  deleteChapter(courseId, chapterId) {
    return request({
      url: `/api/courses/${courseId}/chapter/${chapterId}`,
      method: 'delete'
    })
  },

  // 更新学习进度
  updateLearningProgress(courseId, chapterId, data) {
    return request({
      url: `/api/courses/${courseId}/chapter/${chapterId}/progress`,
      method: 'post',
      data
    })
  }
}

export const learningApi = {
  // 获取学习记录列表
  getLearningRecords(params, userId) {
    const headers = {}
    if (userId) headers['X-User-Id'] = userId

    return request({
      url: '/api/learning-records',
      method: 'get',
      params,
      headers
    })
  },

  // 创建或更新学习记录
  saveOrUpdateRecord(data, userId) {
    const headers = {}
    if (userId) headers['X-User-Id'] = userId

    return request({
      url: '/api/learning-records',
      method: 'post',
      data,
      headers
    })
  },

  // 更新学习进度
  updateProgress(courseId, progress, userId) {
    const headers = {}
    if (userId) headers['X-User-Id'] = userId

    return request({
      url: '/api/learning-records/progress',
      method: 'put',
      params: {
        courseId,
        progress
      },
      headers
    })
  },

  // 获取用户学习统计
  getUserLearningStats(userId) {
    const headers = {}
    if (userId) headers['X-User-Id'] = userId

    return request({
      url: '/api/learning-records/user-stats',
      method: 'get',
      headers
    })
  },

  // 获取课程学习统计
  getCourseLearnStats(courseId) {
    return request({
      url: '/api/learning-records/course-stats',
      method: 'get',
      params: { courseId }
    })
  },

  // 完成课程学习
  completeCourse(courseId, userId) {
    const headers = {}
    if (userId) headers['X-User-Id'] = userId

    return request({
      url: '/api/learning-records/complete',
      method: 'post',
      params: { courseId },
      headers
    })
  }
} 