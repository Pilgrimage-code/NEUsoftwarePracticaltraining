import request from '@/utils/request'

/**
 * 上传图片
 * @param {File} file 图片文件
 * @returns {Promise<Object>} 上传结果
 */
export function uploadImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/api/upload/image',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 上传视频
 * @param {File} file 视频文件
 * @returns {Promise<Object>} 上传结果
 */
export function uploadVideo(file) {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/api/upload/video',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 上传文档
 * @param {File} file 文档文件
 * @returns {Promise<Object>} 上传结果
 */
export function uploadDocument(file) {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/api/upload/document',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 上传其他类型文件
 * @param {File} file 文件
 * @returns {Promise<Object>} 上传结果
 */
export function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/api/upload/file',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 删除文件
 * @param {string} url 文件URL
 * @returns {Promise<Object>} 删除结果
 */
export function deleteFile(url) {
  return request({
    url: '/api/upload/file',
    method: 'delete',
    data: { url }
  })
}

export const uploadApi = {
  uploadImage,
  uploadVideo,
  uploadDocument,
  uploadFile,
  deleteFile
}

export default uploadApi 