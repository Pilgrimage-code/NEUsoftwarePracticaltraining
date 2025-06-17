import request from '@/utils/request'

// 资讯管理API
export const newsApi = {
  // 获取资讯列表
  getNewsList(params) {
    return request({
      url: '/api/news',
      method: 'get',
      params
    })
  },

  // 根据ID获取资讯详情
  getNewsById(id) {
    return request({
      url: `/api/news/${id}`,
      method: 'get'
    })
  },

  // 创建资讯
  createNews(data) {
    return request({
      url: '/api/news',
      method: 'post',
      data
    })
  },

  // 更新资讯
  updateNews(id, data) {
    return request({
      url: `/api/news/${id}`,
      method: 'put',
      data
    })
  },

  // 删除资讯
  deleteNews(id) {
    return request({
      url: `/api/news/${id}`,
      method: 'delete'
    })
  },

  // 批量删除资讯
  batchDeleteNews(ids) {
    return request({
      url: '/api/news/batch',
      method: 'delete',
      data: ids
    })
  },

  // 发布资讯
  publishNews(id) {
    return request({
      url: `/api/news/${id}/publish`,
      method: 'put'
    })
  },

  // 下架资讯
  unpublishNews(id) {
    return request({
      url: `/api/news/${id}/unpublish`,
      method: 'put'
    })
  },

  // 置顶资讯
  topNews(id) {
    return request({
      url: `/api/news/${id}/top`,
      method: 'put'
    })
  },

  // 取消置顶
  untopNews(id) {
    return request({
      url: `/api/news/${id}/untop`,
      method: 'put'
    })
  },

  // 设为热门
  setHotNews(id) {
    return request({
      url: `/api/news/${id}/hot`,
      method: 'put'
    })
  },

  // 取消热门
  unsetHotNews(id) {
    return request({
      url: `/api/news/${id}/unhot`,
      method: 'put'
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
  searchNews(keyword, page = 1, size = 10) {
    return request({
      url: '/api/news/search',
      method: 'get',
      params: { keyword, page, size }
    })
  }
}

