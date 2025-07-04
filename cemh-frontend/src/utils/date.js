/**
 * 日期格式化工具函数
 */

/**
 * 格式化日期（不包含时间）
 * @param {string|Date} dateString 日期字符串或Date对象
 * @returns {string} 格式化后的日期字符串 (YYYY-MM-DD)
 */
export const formatDate = (dateString) => {
  if (!dateString) return '-';
  
  try {
    // 尝试处理各种可能的日期格式
    let date;
    
    // 如果是ISO格式字符串
    if (typeof dateString === 'string') {
      // 标准化时区部分 
      const standardDateString = dateString.replace(/([+-]\d{2})$/, '$1:00');
      date = new Date(standardDateString);
    } else {
      // 如果已经是Date对象或其他格式
      date = new Date(dateString);
    }
    
    // 检查日期是否有效
    if (isNaN(date.getTime())) {
      console.warn('无效的日期格式:', dateString);
      return '-';
    }
    
    // 格式化为YYYY-MM-DD
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    
    return `${year}-${month}-${day}`;
  } catch (error) {
    console.error('日期格式化错误:', error, dateString);
    return '-';
  }
};

/**
 * 格式化日期时间
 * @param {string|Date} dateString 日期字符串或Date对象
 * @returns {string} 格式化后的日期时间字符串 (YYYY-MM-DD HH:MM)
 */
export const formatDateTime = (dateString) => {
  if (!dateString) return '-';
  
  try {
    // 尝试处理各种可能的日期格式
    let date;
    
    // 如果是ISO格式字符串
    if (typeof dateString === 'string') {
      // 标准化时区部分 
      const standardDateString = dateString.replace(/([+-]\d{2})$/, '$1:00');
      date = new Date(standardDateString);
    } else {
      // 如果已经是Date对象或其他格式
      date = new Date(dateString);
    }
    
    // 检查日期是否有效
    if (isNaN(date.getTime())) {
      console.warn('无效的日期格式:', dateString);
      return '-';
    }
    
    // 格式化为YYYY-MM-DD HH:MM
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    
    return `${year}-${month}-${day} ${hours}:${minutes}`;
  } catch (error) {
    console.error('日期格式化错误:', error, dateString);
    return '-';
  }
};

/**
 * 解析日期字符串为Date对象
 * @param {string} dateString 日期字符串
 * @returns {Date|null} 解析后的Date对象，解析失败则返回null
 */
export const parseDate = (dateString) => {
  if (!dateString) return null;
  
  try {
    const date = new Date(dateString);
    return isNaN(date.getTime()) ? null : date;
  } catch (error) {
    console.error('日期解析错误:', error, dateString);
    return null;
  }
}; 