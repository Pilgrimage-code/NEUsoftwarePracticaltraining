import request from '@/utils/request'

// 资讯管理API
export const newsApi = {
  // 获取资讯列表
  getNewsList(params, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: '/api/news',
      method: 'get',
      params,
      headers
    })
  },

  // 根据ID获取资讯详情
  getNewsById(id, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: `/api/news/${id}`,
      method: 'get',
      headers
    })
  },

  // 创建资讯
  createNews(data, tenantId, userId) {
    const headers = {};
    if (tenantId) headers['X-Tenant-Id'] = tenantId;
    if (userId) headers['X-User-Id'] = userId;
    return request.post('/api/news', data, { headers })
    // 字段映射：前端 -> 后端
    const mappedData = {
      title: data.title,
      summary: data.summary,
      content: data.content,
      author: data.author,
      coverImage: data.coverImage, // 前端imagePath -> 后端coverImage
      categoryName: data.category, // 前端category -> 后端categoryName
      tags: data.tags || '',
      source: data.source || '',
      status: data.status || 1, // 默认发布状态
      isTop: data.isTop ? 1 : 0,
      isHot: data.isHot ? 1 : 0,
      remark: data.remark || ''
    }

    return request({
      url: '/api/news',
      method: 'post',
      data: mappedData,
      headers
    })
  },

  // 更新资讯
  updateNews(id, data, tenantId, userId) {
    const headers = {};
    if (tenantId) headers['X-Tenant-Id'] = tenantId;
    if (userId) headers['X-User-Id'] = userId;

    // 字段映射：前端 -> 后端
    const mappedData = {
      title: data.title,
      summary: data.summary,
      content: data.content,
      author: data.author,
      coverImage: data.coverImage, // 前端imagePath -> 后端coverImage
      categoryName: data.category, // 前端category -> 后端categoryName
      tags: data.tags || '',
      source: data.source || '',
      status: data.status || 1,
      isTop: data.isTop ? 1 : 0,
      isHot: data.isHot ? 1 : 0,
      remark: data.remark || ''
    }

    return request({
      url: `/api/news/${id}`,
      method: 'put',
      data: mappedData,
      headers
    })
  },

  // 删除资讯
  deleteNews(id, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: `/api/news/${id}`,
      method: 'delete',
      headers
    })
  },

  // 批量删除资讯
  batchDeleteNews(ids, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: '/api/news/batch',
      method: 'delete',
      data: ids,
      headers
    })
  },

  // 发布资讯
  publishNews(id, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: `/api/news/${id}/publish`,
      method: 'put',
      headers
    })
  },

  // 下架资讯
  unpublishNews(id, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: `/api/news/${id}/unpublish`,
      method: 'put',
      headers
    })
  },

  // 置顶资讯
  topNews(id, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: `/api/news/${id}/top`,
      method: 'put',
      headers
    })
  },

  // 取消置顶
  untopNews(id, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: `/api/news/${id}/untop`,
      method: 'put',
      headers
    })
  },

  // 设为热门
  setHotNews(id, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: `/api/news/${id}/hot`,
      method: 'put',
      headers
    })
  },

  // 取消热门
  unsetHotNews(id, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: `/api/news/${id}/unhot`,
      method: 'put',
      headers
    })
  },

  // 获取资讯分类列表
  getNewsCategories() {
    return request({
      url: '/api/news/categories',
      method: 'get'
    })
  },

  // 获取热门资讯
  getHotNews(limit = 10) {
    return request({
      url: '/api/news/hot',
      method: 'get',
      params: { limit }
    })
  },

  // 获取推荐资讯
  getRecommendNews(limit = 10) {
    return request({
      url: '/api/news/recommend',
      method: 'get',
      params: { limit }
    })
  },

  // 获取最新资讯
  getLatestNews(limit = 10) {
    return request({
      url: '/api/news/latest',
      method: 'get',
      params: { limit }
    })
  },

  // 搜索资讯
  searchNews(keyword, page = 1, size = 10, tenantId) {
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};
    return request({
      url: '/api/news/search',
      method: 'get',
      params: { keyword, page, size },
      headers
    })
  },

  // 增加浏览次数
  incrementViewCount(id) {
    return request({
      url: `/api/news/${id}/view`,
      method: 'put'
    })
  }
}

// 文件上传API
export const uploadApi = {
  // 上传图片
  uploadImage(file, tenantId) {
    const formData = new FormData()
    formData.append('file', file)
    const headers = tenantId ? { 'X-Tenant-Id': tenantId } : {};

    return request({
      url: '/api/upload/image',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data',
        ...headers
      }
    })
  }
}


