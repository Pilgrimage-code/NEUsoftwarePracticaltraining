<template>
  <div class="news-detail modern-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <el-button 
            type="text" 
            @click="$router.go(-1)"
            class="back-btn"
          >
            <el-icon><ArrowLeft /></el-icon>
            返回
          </el-button>
        </div>
        <div class="header-actions">
          <el-button 
            @click="shareNews"
            class="modern-btn secondary"
          >
            <el-icon><Share /></el-icon>
            分享
          </el-button>
          <el-button 
            type="primary" 
            @click="editNews"
            class="modern-btn primary"
            v-if="canEdit"
          >
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
        </div>
      </div>
    </div>

    <!-- 资讯内容 -->
    <div class="news-content">
      <!-- 资讯头部信息 -->
      <div class="news-header">
        <div class="news-category">
          <el-tag :type="getCategoryType(newsInfo.category)">
            {{ getCategoryText(newsInfo.category) }}
          </el-tag>
        </div>
        <h1 class="news-title">{{ newsInfo.title }}</h1>
        <div class="news-meta">
          <div class="meta-item">
            <el-icon><User /></el-icon>
            <span>{{ newsInfo.author }}</span>
          </div>
          <div class="meta-item">
            <el-icon><Clock /></el-icon>
            <span>{{ formatDateTime(newsInfo.publishTime) }}</span>
          </div>
          <div class="meta-item">
            <el-icon><View /></el-icon>
            <span>{{ newsInfo.viewCount }} 次阅读</span>
          </div>
          <div class="meta-item" v-if="newsInfo.commentCount > 0">
            <el-icon><ChatDotRound /></el-icon>
            <span>{{ newsInfo.commentCount }} 条评论</span>
          </div>
        </div>
        <div class="news-tags" v-if="newsInfo.tags && newsInfo.tags.length > 0">
          <el-tag 
            v-for="tag in newsInfo.tags" 
            :key="tag"
            size="small"
            effect="plain"
            class="tag-item"
          >
            {{ tag }}
          </el-tag>
        </div>
      </div>

      <!-- 封面图片 -->
      <div class="news-cover" v-if="newsInfo.coverImage">
        <img :src="newsInfo.coverImage" :alt="newsInfo.title" />
      </div>

      <!-- 资讯摘要 -->
      <div class="news-summary" v-if="newsInfo.summary">
        <div class="summary-content">
          {{ newsInfo.summary }}
        </div>
      </div>

      <!-- 资讯正文 -->
      <div class="news-body">
        <div class="content-wrapper" v-html="newsInfo.content"></div>
      </div>

      <!-- 资讯底部 -->
      <div class="news-footer">
        <div class="news-actions">
          <el-button 
            :type="isLiked ? 'primary' : 'default'"
            @click="toggleLike"
            class="action-btn"
          >
            <el-icon><StarFilled v-if="isLiked" /><Star v-else /></el-icon>
            {{ isLiked ? '已点赞' : '点赞' }} ({{ newsInfo.likeCount }})
          </el-button>
          <el-button 
            :type="isCollected ? 'primary' : 'default'"
            @click="toggleCollect"
            class="action-btn"
          >
            <el-icon><CollectionTag v-if="isCollected" /><Collection v-else /></el-icon>
            {{ isCollected ? '已收藏' : '收藏' }} ({{ newsInfo.collectCount }})
          </el-button>
          <el-button 
            @click="shareNews"
            class="action-btn"
          >
            <el-icon><Share /></el-icon>
            分享
          </el-button>
        </div>
        
        <div class="news-info">
          <div class="info-item">
            <span class="info-label">发布时间：</span>
            <span class="info-value">{{ formatDateTime(newsInfo.publishTime) }}</span>
          </div>
          <div class="info-item" v-if="newsInfo.updateTime">
            <span class="info-label">更新时间：</span>
            <span class="info-value">{{ formatDateTime(newsInfo.updateTime) }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">阅读量：</span>
            <span class="info-value">{{ newsInfo.viewCount }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 相关资讯 -->
    <div class="related-news" v-if="relatedNews.length > 0">
      <div class="modern-card">
        <div class="card-header">
          <h2>相关资讯</h2>
        </div>
        <div class="card-body">
          <div class="related-list">
            <div 
              v-for="item in relatedNews" 
              :key="item.id"
              class="related-item"
              @click="goToNews(item.id)"
            >
              <div class="related-cover" v-if="item.coverImage">
                <img :src="item.coverImage" :alt="item.title" />
              </div>
              <div class="related-content">
                <h3 class="related-title">{{ item.title }}</h3>
                <p class="related-summary">{{ item.summary }}</p>
                <div class="related-meta">
                  <span>{{ formatDateTime(item.publishTime) }}</span>
                  <span>{{ item.viewCount }} 次阅读</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 评论区 -->
    <div class="comments-section" v-if="newsInfo.allowComment">
      <div class="modern-card">
        <div class="card-header">
          <h2>评论 ({{ comments.length }})</h2>
        </div>
        <div class="card-body">
          <!-- 发表评论 -->
          <div class="comment-form">
            <el-input
              v-model="newComment"
              type="textarea"
              :rows="3"
              placeholder="写下你的评论..."
              class="modern-input"
            />
            <div class="comment-actions">
              <el-button 
                type="primary" 
                @click="submitComment"
                :disabled="!newComment.trim()"
                class="modern-btn primary"
              >
                发表评论
              </el-button>
            </div>
          </div>

          <!-- 评论列表 -->
          <div class="comments-list" v-if="comments.length > 0">
            <div 
              v-for="comment in comments" 
              :key="comment.id"
              class="comment-item"
            >
              <div class="comment-avatar">
                <el-avatar :src="comment.avatar" :size="40">
                  {{ comment.author.charAt(0) }}
                </el-avatar>
              </div>
              <div class="comment-content">
                <div class="comment-header">
                  <span class="comment-author">{{ comment.author }}</span>
                  <span class="comment-time">{{ formatDateTime(comment.createTime) }}</span>
                </div>
                <div class="comment-text">{{ comment.content }}</div>
                <div class="comment-actions">
                  <el-button 
                    type="text" 
                    size="small"
                    @click="replyComment(comment)"
                  >
                    回复
                  </el-button>
                  <el-button 
                    type="text" 
                    size="small"
                    @click="likeComment(comment)"
                  >
                    <el-icon><Star /></el-icon>
                    {{ comment.likeCount || 0 }}
                  </el-button>
                </div>
              </div>
            </div>
          </div>

          <!-- 空状态 -->
          <div class="empty-comments" v-else>
            <el-empty description="暂无评论，快来发表第一条评论吧！" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()

// 响应式数据
const newsInfo = ref({
  id: '',
  title: '',
  category: '',
  author: '',
  publishTime: '',
  updateTime: '',
  viewCount: 0,
  likeCount: 0,
  collectCount: 0,
  commentCount: 0,
  coverImage: '',
  summary: '',
  content: '',
  tags: [],
  allowComment: true
})

const relatedNews = ref([])
const comments = ref([])
const newComment = ref('')
const isLiked = ref(false)
const isCollected = ref(false)
const canEdit = ref(false)

// 方法
const loadNewsDetail = async () => {
  try {
    const newsId = route.params.id
    // 这里应该调用API获取资讯详情
    // const response = await getNewsDetail(newsId)
    
    // 模拟数据
    newsInfo.value = {
      id: newsId,
      title: '公司2024年第一季度业绩报告发布',
      category: 'company',
      author: '张三',
      publishTime: '2024-06-20 10:00:00',
      updateTime: '2024-06-20 14:30:00',
      viewCount: 1256,
      likeCount: 89,
      collectCount: 45,
      commentCount: 12,
      coverImage: 'https://via.placeholder.com/800x450',
      summary: '本季度公司业绩表现优异，各项指标均超预期完成，营收同比增长25%，净利润同比增长30%。',
      content: `
        <p>尊敬的各位股东、员工和合作伙伴：</p>
        <p>我们很高兴向大家报告公司2024年第一季度的业绩情况。本季度，公司在全体员工的共同努力下，取得了优异的业绩表现。</p>
        <h3>主要业绩指标</h3>
        <ul>
          <li>营业收入：5.2亿元，同比增长25%</li>
          <li>净利润：8500万元，同比增长30%</li>
          <li>毛利率：45.2%，同比提升2.1个百分点</li>
          <li>新增客户：1200家，同比增长40%</li>
        </ul>
        <h3>业务亮点</h3>
        <p>本季度，公司在以下几个方面取得了重要进展：</p>
        <ol>
          <li><strong>产品创新</strong>：推出了3款新产品，获得市场积极反响</li>
          <li><strong>市场拓展</strong>：成功进入5个新的地区市场</li>
          <li><strong>技术升级</strong>：完成了核心系统的升级改造</li>
          <li><strong>团队建设</strong>：新增员工200人，团队实力进一步增强</li>
        </ol>
        <p>展望未来，我们将继续坚持创新驱动发展战略，为客户提供更优质的产品和服务。</p>
      `,
      tags: ['业绩报告', '第一季度', '财务数据'],
      allowComment: true
    }

    // 模拟相关资讯
    relatedNews.value = [
      {
        id: '2',
        title: '公司荣获"年度最佳雇主"称号',
        summary: '在刚刚结束的年度评选中，公司凭借优秀的企业文化和员工关怀政策...',
        coverImage: 'https://via.placeholder.com/200x120',
        publishTime: '2024-06-18 15:30:00',
        viewCount: 856
      },
      {
        id: '3',
        title: '新产品发布会圆满举行',
        summary: '6月15日，公司在北京举行了新产品发布会，正式推出了三款创新产品...',
        coverImage: 'https://via.placeholder.com/200x120',
        publishTime: '2024-06-15 09:00:00',
        viewCount: 1024
      }
    ]

    // 模拟评论数据
    comments.value = [
      {
        id: 1,
        author: '李四',
        avatar: '',
        content: '业绩表现确实不错，期待下个季度继续保持！',
        createTime: '2024-06-20 11:30:00',
        likeCount: 5
      },
      {
        id: 2,
        author: '王五',
        avatar: '',
        content: '新产品的市场反响如何？希望能看到更详细的数据。',
        createTime: '2024-06-20 12:15:00',
        likeCount: 3
      }
    ]

    // 增加阅读量
    newsInfo.value.viewCount++
  } catch (error) {
    ElMessage.error('加载资讯详情失败')
  }
}

const editNews = () => {
  router.push(`/dashboard/news/edit/${newsInfo.value.id}`)
}

const shareNews = () => {
  // 这里可以实现分享功能
  ElMessage.success('分享链接已复制到剪贴板')
}

const toggleLike = async () => {
  try {
    isLiked.value = !isLiked.value
    if (isLiked.value) {
      newsInfo.value.likeCount++
      ElMessage.success('点赞成功')
    } else {
      newsInfo.value.likeCount--
      ElMessage.success('取消点赞')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const toggleCollect = async () => {
  try {
    isCollected.value = !isCollected.value
    if (isCollected.value) {
      newsInfo.value.collectCount++
      ElMessage.success('收藏成功')
    } else {
      newsInfo.value.collectCount--
      ElMessage.success('取消收藏')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const submitComment = async () => {
  if (!newComment.value.trim()) return
  
  try {
    const comment = {
      id: Date.now(),
      author: '当前用户',
      avatar: '',
      content: newComment.value,
      createTime: new Date().toISOString(),
      likeCount: 0
    }
    
    comments.value.unshift(comment)
    newsInfo.value.commentCount++
    newComment.value = ''
    
    ElMessage.success('评论发表成功')
  } catch (error) {
    ElMessage.error('发表评论失败')
  }
}

const replyComment = (comment) => {
  newComment.value = `@${comment.author} `
}

const likeComment = (comment) => {
  comment.likeCount = (comment.likeCount || 0) + 1
  ElMessage.success('点赞成功')
}

const goToNews = (newsId) => {
  router.push(`/dashboard/news/${newsId}`)
}

// 工具方法
const formatDateTime = (dateTime) => {
  return dayjs(dateTime).format('YYYY-MM-DD HH:mm')
}

const getCategoryType = (category) => {
  const types = {
    company: 'primary',
    industry: 'success',
    product: 'warning',
    activity: 'info',
    other: 'default'
  }
  return types[category] || 'default'
}

const getCategoryText = (category) => {
  const texts = {
    company: '公司新闻',
    industry: '行业动态',
    product: '产品更新',
    activity: '活动通知',
    other: '其他'
  }
  return texts[category] || '未知'
}

onMounted(() => {
  loadNewsDetail()
})
</script>

<style scoped>
.news-detail {
  padding: var(--spacing-2xl);
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: var(--spacing-2xl);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  color: var(--text-secondary);
  transition: color var(--transition-fast);
}

.back-btn:hover {
  color: var(--primary-color);
}

.header-actions {
  display: flex;
  gap: var(--spacing-md);
}

.news-content {
  background: var(--bg-primary);
  border-radius: var(--radius-2xl);
  padding: var(--spacing-3xl);
  box-shadow: var(--shadow-md);
  margin-bottom: var(--spacing-2xl);
}

.news-header {
  margin-bottom: var(--spacing-2xl);
}

.news-category {
  margin-bottom: var(--spacing-lg);
}

.news-title {
  font-size: var(--font-size-4xl);
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1.2;
  margin: 0 0 var(--spacing-lg) 0;
}

.news-meta {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-lg);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.news-tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-sm);
}

.tag-item {
  margin: 0;
}

.news-cover {
  margin-bottom: var(--spacing-2xl);
}

.news-cover img {
  width: 100%;
  height: auto;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-md);
}

.news-summary {
  margin-bottom: var(--spacing-2xl);
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  border-left: 4px solid var(--primary-color);
}

.summary-content {
  font-size: var(--font-size-lg);
  color: var(--text-secondary);
  line-height: 1.6;
  font-style: italic;
}

.news-body {
  margin-bottom: var(--spacing-2xl);
}

.content-wrapper {
  font-size: var(--font-size-md);
  line-height: 1.8;
  color: var(--text-primary);
}

.content-wrapper :deep(h3) {
  font-size: var(--font-size-xl);
  font-weight: 600;
  color: var(--text-primary);
  margin: var(--spacing-2xl) 0 var(--spacing-lg) 0;
}

.content-wrapper :deep(p) {
  margin-bottom: var(--spacing-lg);
}

.content-wrapper :deep(ul),
.content-wrapper :deep(ol) {
  margin: var(--spacing-lg) 0;
  padding-left: var(--spacing-2xl);
}

.content-wrapper :deep(li) {
  margin-bottom: var(--spacing-sm);
}

.news-footer {
  border-top: 1px solid var(--border-light);
  padding-top: var(--spacing-lg);
}

.news-actions {
  display: flex;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.action-btn {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.news-info {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-lg);
}

.info-item {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.info-label {
  font-weight: 500;
}

.related-news,
.comments-section {
  margin-bottom: var(--spacing-2xl);
}

.card-header {
  margin-bottom: var(--spacing-lg);
}

.card-header h2 {
  font-size: var(--font-size-xl);
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.related-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.related-item {
  display: flex;
  gap: var(--spacing-lg);
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.related-item:hover {
  background: var(--bg-tertiary);
  transform: translateY(-1px);
}

.related-cover {
  flex-shrink: 0;
  width: 120px;
  height: 80px;
  border-radius: var(--radius-md);
  overflow: hidden;
}

.related-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.related-content {
  flex: 1;
}

.related-title {
  font-size: var(--font-size-md);
  font-weight: 500;
  color: var(--text-primary);
  margin: 0 0 var(--spacing-sm) 0;
  line-height: 1.4;
}

.related-summary {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  line-height: 1.5;
  margin: 0 0 var(--spacing-sm) 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.related-meta {
  display: flex;
  gap: var(--spacing-md);
  font-size: var(--font-size-xs);
  color: var(--text-tertiary);
}

.comment-form {
  margin-bottom: var(--spacing-2xl);
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: var(--spacing-md);
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.comment-item {
  display: flex;
  gap: var(--spacing-md);
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-sm);
}

.comment-author {
  font-weight: 500;
  color: var(--text-primary);
}

.comment-time {
  font-size: var(--font-size-xs);
  color: var(--text-tertiary);
}

.comment-text {
  color: var(--text-primary);
  line-height: 1.6;
  margin-bottom: var(--spacing-sm);
}

.comment-actions {
  display: flex;
  gap: var(--spacing-md);
}

.empty-comments {
  text-align: center;
  padding: var(--spacing-3xl) 0;
}

@media (max-width: 768px) {
  .news-detail {
    padding: var(--spacing-lg);
  }
  
  .news-content {
    padding: var(--spacing-lg);
  }
  
  .news-title {
    font-size: var(--font-size-2xl);
  }
  
  .news-meta {
    flex-direction: column;
    gap: var(--spacing-sm);
  }
  
  .related-item {
    flex-direction: column;
  }
  
  .related-cover {
    width: 100%;
    height: 150px;
  }
  
  .comment-item {
    flex-direction: column;
  }
}
</style>

