import request from '@/utils/request'

// 获取学习记录列表
export function getLearningRecords(params) {
  return request({
    url: '/api/learning-records',
    method: 'get',
    params: {
      page: params.current || 1,
      size: params.size || 10,
      userId: params.userId,
      courseId: params.courseId,
      status: params.status
    },
    headers: {
      'X-User-Id': params.userId || '1'
    }
  })
}

// 获取用户学习统计
export function getLearningStats(userId) {
  return request({
    url: '/api/learning-records/user-stats',
    method: 'get',
    headers: {
      'X-User-Id': userId || '1'
    }
  })
}

// 更新学习进度
export function updateProgress(data) {
  return request({
    url: `/api/learning-records/progress`,
    method: 'put',
    params: {
      courseId: data.courseId,
      progress: data.progress,
      chapterId: data.chapterId
    },
    headers: {
      'X-User-Id': data.userId || '1'
    }
  })
}

// 完成课程
export function completeCourse(data) {
  return request({
    url: '/api/learning-records/complete',
    method: 'post',
    params: {
      courseId: data.courseId
    },
    headers: {
      'X-User-Id': data.userId || '1'
    }
  })
}

// 获取用户课程学习记录
export function getUserCourseRecord(userId, courseId) {
  return request({
    url: '/api/learning-records',
    method: 'get',
    params: {
      page: 1,
      size: 1,
      userId: userId,
      courseId: courseId
    },
    headers: {
      'X-User-Id': userId || '1'
    }
  })
}

// 创建或更新学习记录
export function saveOrUpdateRecord(data) {
  return request({
    url: '/api/learning-records',
    method: 'post',
    data,
    headers: {
      'X-User-Id': data.userId || '1'
    }
  })
}

export const learningApi = {
  getLearningRecords,
  getLearningStats,
  updateProgress,
  completeCourse,
  getUserCourseRecord,
  saveOrUpdateRecord
}

export default learningApi 