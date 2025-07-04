<template>
  <div class="course-detail">
    <!-- 课程基本信息 -->
    <div class="course-header" v-loading="loading">
      <div class="course-cover">
        <el-image
          v-if="course.coverImage"
          :src="course.coverImage"
          fit="cover"
          class="cover-image"
          @error="handleImageError"
        />
        <div v-else class="no-cover">
          <el-icon><Picture /></el-icon>
          <span>暂无封面</span>
        </div>
      </div>
      
      <div class="course-info">
        <h1 class="course-title">{{ course.courseName }}</h1>
        <div class="course-meta">
          <div class="meta-item">
            <el-icon><User /></el-icon>
            <span>讲师：{{ course.instructor || course.courseAuthor }}</span>
          </div>
          <div class="meta-item">
            <el-icon><Clock /></el-icon>
            <span>时长：{{ formatDuration(course.duration) }}</span>
          </div>
          <div class="meta-item">
            <el-icon><Star /></el-icon>
            <span>评分：{{ course.rating || 0 }}分</span>
          </div>
          <div class="meta-item">
            <el-icon><View /></el-icon>
            <span>浏览：{{ course.viewCount || 0 }}次</span>
          </div>
        </div>
        
        <div class="course-price" v-if="!course.isFree">
          <span class="price">¥{{ course.price }}</span>
        </div>
        <div class="course-free" v-else>
          <el-tag type="success">免费课程</el-tag>
        </div>
        
        <div class="course-actions">
          <el-button 
            type="primary" 
            size="large" 
            @click="startLearning"
            :loading="actionLoading"
          >
            <el-icon><VideoPlay /></el-icon>
            {{ learningRecord ? '继续学习' : '开始学习' }}
          </el-button>
          
          <el-button 
            size="large" 
            @click="addToFavorites"
            :loading="favoriteLoading"
          >
            <el-icon><Star /></el-icon>
            收藏课程
          </el-button>
        </div>
        
        <!-- 学习进度 -->
        <div class="learning-progress" v-if="learningRecord">
          <div class="progress-text">
            学习进度：{{ learningRecord.progress }}%
          </div>
          <el-progress 
            :percentage="learningRecord.progress" 
            :color="getProgressColor(learningRecord.progress)"
          />
        </div>
      </div>
    </div>
    
    <!-- 课程详情内容 -->
    <div class="course-content">
      <el-tabs v-model="activeTab" type="card">
        <el-tab-pane label="课程介绍" name="intro">
          <div class="course-description">
            <h3>课程简介</h3>
            <div class="description-content" v-html="course.description || course.courseIntro || '暂无课程介绍'"></div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="课程目录" name="chapters">
          <div class="course-chapters">
            <h3>课程目录</h3>
            <div v-if="chapters.length === 0" class="no-chapters">
              暂无课程章节
            </div>
            <div v-else class="chapter-list">
              <div 
                v-for="(chapter, index) in chapters" 
                :key="chapter.id"
                class="chapter-item"
                @click="playChapter(chapter)"
              >
                <div class="chapter-number">{{ index + 1 }}</div>
                <div class="chapter-info">
                  <div class="chapter-title">{{ chapter.chapterName || chapter.title }}</div>
                  <div class="chapter-duration">{{ formatDuration(chapter.duration) }}</div>
                </div>
                <div class="chapter-status">
                  <el-tag 
                    :type="getChapterStatusType(chapter.status)" 
                    size="small"
                  >
                    {{ getChapterStatusText(chapter.status) }}
                  </el-tag>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="评价反馈" name="reviews">
          <div class="course-reviews">
            <h3>学员评价</h3>
            <div class="no-reviews">
              暂无评价内容
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <!-- 视频播放对话框 -->
    <el-dialog
      :title="currentChapter ? currentChapter.chapterName : '视频学习'"
      v-model="videoDialogVisible"
      fullscreen
      destroy-on-close
      :append-to-body="true"
      @open="handleDialogOpen"
      @close="handleDialogClose"
    >
      <div class="video-player-container">
        <video
          v-if="currentVideo"
          ref="videoPlayer"
          controls
          class="video-player"
          @timeupdate="handleVideoProgress"
          @ended="handleVideoCompleted"
          @pause="handleVideoPause"
          @play="handleVideoPlay"
          @error="handleVideoError"
          :src="formatVideoUrl(currentVideo)"
        >
          您的浏览器不支持视频播放。
        </video>
        <div v-else class="no-video">
          暂无视频内容
        </div>
        
        <div class="video-controls">
          <el-button type="primary" @click="stopLearning">
            <el-icon><VideoPause /></el-icon>
            终止学习
          </el-button>
          
          <el-button type="success" @click="playNextChapter" :disabled="!hasNextChapter">
            <el-icon><ArrowRight /></el-icon>
            下一章节
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed, nextTick, onUnmounted, onBeforeUnmount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Picture,
  User,
  Clock,
  Star,
  View,
  VideoPlay,
  Check,
  VideoPause,
  ArrowRight
} from '@element-plus/icons-vue'
import { courseApi } from '@/api/course'
import { learningApi } from '@/api/learning'
import { useUserStore } from '@/store/user'
import { updateProgress, getUserCourseRecord } from '@/api/learning'

export default {
  name: 'CourseDetail',
  components: {
    Picture,
    User,
    Clock,
    Star,
    View,
    VideoPlay,
    Check,
    VideoPause,
    ArrowRight
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const userStore = useUserStore()
    
    const courseId = computed(() => parseInt(route.params.id))
    const userId = computed(() => userStore.userId || 1)
    
    // 数据状态
    const loading = ref(false)
    const actionLoading = ref(false)
    const favoriteLoading = ref(false)
    const videoDialogVisible = ref(false)
    
    const course = reactive({})
    const chapters = ref([])
    const learningRecord = ref(null)
    const completedChapters = ref([])
    
    const activeTab = ref('intro')
    const currentVideo = ref('')
    const videoPlayer = ref(null)
    
    const relatedCourses = ref([])
    const isFavorite = ref(false)
    
    // 视频相关
    const currentTime = ref(0)
    const duration = ref(0)
    const currentChapter = ref(null)
    
    // 判断是否有下一章节
    const hasNextChapter = computed(() => {
      if (!currentChapter.value || !chapters.value.length) return false;
      
      // 找到当前章节的索引
      const currentIndex = chapters.value.findIndex(chapter => 
        chapter.id === currentChapter.value.id
      );
      
      // 如果找到当前章节且不是最后一章，则有下一章
      return currentIndex !== -1 && currentIndex < chapters.value.length - 1;
    });
    
    let progressUpdateTimer = null
    
    // 获取课程详情
    const loadCourseDetail = async () => {
      loading.value = true;
      try {
        const res = await courseApi.getCourseById(courseId.value);
        if (res.code === 200) {
          Object.assign(course, res.data);
          // 增加浏览次数
          await courseApi.incrementViewCount(courseId.value);

          // 获取章节列表
          await fetchCourseChapters();
          
          // 获取学习记录
          await fetchLearningRecord(courseId.value);
          
          // 获取相关课程
          await fetchRelatedCourses();

          // 检查URL中是否有指定的章节ID
          const chapterId = route.query.chapterId;
          if (chapterId && chapters.value.length > 0) {
            const targetChapter = chapters.value.find(chapter => chapter.id == chapterId);
            if (targetChapter) {
              // 稍微延迟一下，确保页面已经加载完成
              setTimeout(() => {
                playChapter(targetChapter);
              }, 300);
            }
          }
        } else {
          ElMessage.error(res.message || "获取课程详情失败");
        }
      } catch (error) {
        console.error("获取课程详情失败:", error);
        ElMessage.error("获取课程详情失败");
      } finally {
        loading.value = false;
      }
    };

    // 获取课程章节
    const fetchCourseChapters = async () => {
      try {
        const res = await courseApi.getCourseChapters(courseId.value);
        if (res.code === 200) {
          chapters.value = res.data || [];
        } else {
          console.error("获取章节列表失败:", res.message);
        }
      } catch (error) {
        console.error("获取章节列表异常:", error);
      }
    };
    
    // 获取学习记录
    const fetchLearningRecord = async (courseId) => {
      try {
        const res = await getUserCourseRecord(userId.value, courseId)
        if (res.code === 200) {
          learningRecord.value = res.data
        }
      } catch (error) {
        console.error('获取学习记录失败:', error)
      }
    }
    
    // 获取相关课程
    const fetchRelatedCourses = async () => {
      try {
        const res = await courseApi.getLatestCourses(5)
        if (res.code === 200) {
          relatedCourses.value = res.data.filter(c => c.id !== course.id)
        }
      } catch (error) {
        console.error('获取相关课程失败:', error)
      }
    }
    
    // 开始学习
    const startLearning = async () => {
      actionLoading.value = true
      try {
        // 如果有视频URL，直接播放
        if (course.videoUrl) {
          currentVideo.value = course.videoUrl
          videoDialogVisible.value = true
          
          // 创建或更新学习记录
          if (!learningRecord.value) {
            // 创建新记录
            await learningApi.saveOrUpdateRecord({
              userId: userId.value,
              courseId: courseId.value,
              progress: 0,
              status: 0 // 未开始状态
            })
          } else if (learningRecord.value.status === 0) {
            // 更新状态为学习中
            await learningApi.saveOrUpdateRecord({
              ...learningRecord.value,
              status: 1 // 学习中状态
            })
          }
          await fetchLearningRecord(courseId.value)
        } else {
          ElMessage.warning('该课程暂无视频内容')
        }
      } catch (error) {
        console.error('开始学习失败:', error)
        ElMessage.error('开始学习失败')
      } finally {
        actionLoading.value = false
      }
    }
    
    // 添加收藏
    const addToFavorites = async () => {
      favoriteLoading.value = true
      try {
        isFavorite.value = !isFavorite.value
        ElMessage.success(isFavorite.value ? '收藏成功' : '取消收藏成功')
      } catch (error) {
        console.error('收藏失败:', error)
        ElMessage.error('收藏失败')
      } finally {
        favoriteLoading.value = false
      }
    }
    
    // 播放章节
    const playChapter = async (chapter) => {
      try {
        // 先关闭现有对话框（如果已经打开）
        if (videoDialogVisible.value) {
          videoDialogVisible.value = false;
          await nextTick(); // 等待DOM更新
        }
        
        // 设置当前章节和视频URL
        currentChapter.value = chapter;
        currentVideo.value = chapter.videoUrl;
        console.log("准备播放视频:", formatVideoUrl(currentVideo.value));
        
        // 稍微延迟对话框的打开，确保DOM已更新
        setTimeout(() => {
          videoDialogVisible.value = true;
          
          // 更新学习进度
          if (userId.value) {
            learningApi.saveOrUpdateRecord({
              userId: userId.value,
              courseId: courseId.value,
              lastChapterId: chapter.id,
              progress: 0,
              status: 1 // 学习中状态
            }).then(() => {
              // 重新获取学习记录
              fetchLearningRecord(courseId.value);
            });
          }
        }, 100);
      } catch (error) {
        console.error('播放视频失败:', error);
        ElMessage.error('播放视频失败: ' + (error.message || '未知错误'));
      }
    }
    
    // 获取章节状态类型
    const getChapterStatusType = (status) => {
      switch (status) {
        case 0: return 'info'    // 未开始
        case 1: return 'primary' // 学习中
        case 2: return 'success' // 已完成
        case 3: return 'warning' // 已暂停
        default: return 'info'
      }
    }
    
    // 获取章节状态文本
    const getChapterStatusText = (status) => {
      switch (status) {
        case 0: return '未开始'
        case 1: return '学习中'
        case 2: return '已完成'
        case 3: return '已暂停'
        default: return '未知'
      }
    }
    
    // 视频进度更新
    const handleVideoProgress = () => {
      if (!videoPlayer.value || !learningRecord.value) return
      
      const video = videoPlayer.value
      // 确保视频duration有效，避免NaN错误
      if (!video.duration || isNaN(video.duration) || video.duration <= 0) return
      
      const progress = Math.round((video.currentTime / video.duration) * 100)
      // 确保进度是有效的数字
      if (isNaN(progress)) return
      
      // 每10秒更新一次进度
      if (video.currentTime % 10 < 0.5) {
        updateLearningProgress(progress)
      }
    }
    
    // 视频播放完成
    const handleVideoCompleted = () => {
      updateLearningProgress(100, 2) // 更新进度为100%，状态为已完成
      ElMessage.success('恭喜您完成了本课程的学习！')
    }
    
    // 更新学习进度
    const updateLearningProgress = async (progress, status) => {
      try {
        if (!learningRecord.value) return
        
        // 确保进度是有效的数字
        if (isNaN(progress) || progress === null || progress === undefined) {
          console.warn('无效的进度值:', progress);
          progress = 0; // 使用默认值
        }
        
        // 确保进度是整数
        const validProgress = Math.max(0, Math.min(100, Math.round(progress)));
        
        const newStatus = status || (validProgress > 0 ? 1 : 0) // 如果没有指定状态，根据进度判断
        
        await updateProgress({
          userId: userId.value,
          courseId: courseId.value,
          progress: validProgress,
          status: newStatus
        })
        
        if (learningRecord.value) {
          learningRecord.value.progress = validProgress
          learningRecord.value.status = newStatus
        }
      } catch (error) {
        console.error('更新学习进度失败:', error)
      }
    }
    
    // 工具函数
    const formatDuration = (minutes) => {
      if (!minutes) return '0分钟'
      const hours = Math.floor(minutes / 60)
      const mins = minutes % 60
      return hours > 0 ? `${hours}小时${mins}分钟` : `${mins}分钟`
    }
    
    const getProgressColor = (progress) => {
      if (progress < 30) return '#909399'
      if (progress < 70) return '#e6a23c'
      return '#67c23a'
    }
    
    const isChapterCompleted = (chapterId) => {
      return completedChapters.value.includes(chapterId)
    }
    
    // 处理图片加载错误
    const handleImageError = (e) => {
      e.target.src = '/default-course.png'
    }
    
    // 处理视频播放错误
    const handleVideoError = (error) => {
      console.error('视频加载错误:', error);
      // 不显示错误消息，静默处理
      console.log('尝试使用备用方法加载视频');
      
      // 如果视频元素存在，尝试重新加载
      if (videoPlayer.value) {
        setTimeout(() => {
          try {
            videoPlayer.value.load();
          } catch (e) {
            console.error('重新加载视频失败:', e);
          }
        }, 500);
      }
    }
    
    // 格式化视频URL
    const formatVideoUrl = (url) => {
      if (!url) return '';

      try {
        console.log('原始视频URL:', url);
        // 移除可能的空格和特殊字符
        url = url.trim();
        
        // 使用静态视频路径进行测试（如果视频加载失败）
        // 如果是测试环境，可以使用一个已知可用的视频
        if (import.meta.env.DEV && url.includes('test_mode')) {
          return 'https://www.w3schools.com/html/mov_bbb.mp4'; // 使用公共可用的测试视频
        }
        
        // 如果URL已经是完整格式（http或https开头），直接返回
        if (url.match(/^https?:\/\//)) {
          console.log('视频URL已经是完整URL:', url);
          return url;
        }
        
        const baseUrl = import.meta.env.VITE_APP_BASE_API || 'http://localhost:8080';
        
        // 处理相对路径，确保斜杠正确
        if (!url.startsWith('/')) {
          url = '/' + url;
        }
        
        // 1. 处理类似 /uploads/20250702_UUID.mp4 格式的URL
        if (url.match(/\/uploads\/\d{8}_[a-f0-9-]+\.\w+$/)) {
          const fullUrl = `${baseUrl}${url}`;
          console.log('处理日期格式视频URL:', fullUrl);
          return fullUrl;
        }
        
        // 2. 处理类似 /uploads/videos/2025/07/03/UUID.mp4 格式的URL
        if (url.match(/\/uploads\/videos\/\d{4}\/\d{2}\/\d{2}\/[a-f0-9-]+\.\w+$/)) {
          const fullUrl = `${baseUrl}${url}`;
          console.log('处理目录结构视频URL:', fullUrl);
          return fullUrl;
        }
        
        // 3. 处理纯文件名，如 UUID.mp4
        if (!url.includes('/') || url === `/${url.split('/').pop()}`) {
          const fullUrl = `${baseUrl}/uploads/${url.replace(/^\//, '')}`;
          console.log('处理纯文件名:', fullUrl);
          return fullUrl;
        }
        
        // 4. 处理以 /uploads 开头但不符合上述格式的URL
        if (url.startsWith('/uploads/')) {
          const fullUrl = `${baseUrl}${url}`;
          console.log('处理其他uploads格式URL:', fullUrl);
          return fullUrl;
        }
        
        // 5. 最后处理其他格式的路径，添加基本前缀
        const fullUrl = `${baseUrl}${url}`;
        console.log('处理其他格式路径:', fullUrl);
        return fullUrl;
      } catch (e) {
        console.error('格式化视频URL时出错:', e);
        // 发生错误时返回原始URL，避免完全无法播放
        return url;
      }
    }
    
    // 视频对话框打开事件处理
    const handleDialogOpen = () => {
      console.log('视频对话框已打开');
      
      // 确保当前视频URL是有效的
      if (currentVideo.value) {
        const formattedUrl = formatVideoUrl(currentVideo.value);
        console.log('当前视频URL:', formattedUrl);
      }
      
      // 给视频元素一点时间来初始化并加载
      nextTick(() => {
        if (videoPlayer.value) {
          console.log('视频元素已找到，准备播放');
          
          // 设置视频错误处理
          videoPlayer.value.onerror = function(e) {
            console.error('视频加载出错:', e);
            
            // 这次我们显示错误消息给用户，这样他们知道发生了什么
            ElMessage.error({
              message: '视频加载失败，请稍后再试',
              duration: 3000,
              showClose: true
            });
            
            // 尝试使用备用方法重新加载视频
            setTimeout(() => {
              try {
                console.log('尝试重新加载视频...');
                // 再次获取格式化的URL
                const formattedUrl = formatVideoUrl(currentVideo.value);
                videoPlayer.value.src = formattedUrl;
                videoPlayer.value.load();
              } catch (err) {
                console.error('重新加载视频失败:', err);
              }
            }, 1000);
          };
          
          // 强制重新设置src以确保视频被正确加载
          try {
            const formattedUrl = formatVideoUrl(currentVideo.value);
            videoPlayer.value.src = formattedUrl;
            videoPlayer.value.load();
            
            // 等待视频加载完成
            videoPlayer.value.onloadedmetadata = function() {
              console.log('视频元数据已加载，时长:', videoPlayer.value.duration);
              
              // 尝试播放视频
              try {
                const playPromise = videoPlayer.value.play();
                if (playPromise !== undefined) {
                  playPromise
                    .then(() => {
                      console.log('视频开始播放');
                    })
                    .catch(error => {
                      console.log('视频自动播放失败，需要用户手动点击播放:', error);
                      // 显示提示消息
                      ElMessage.info('请点击播放按钮开始学习');
                    });
                }
              } catch (e) {
                console.error('播放视频时出错:', e);
              }
            };
          } catch (e) {
            console.error('加载视频时出错:', e);
          }
        }
      });
    };
    
    // 视频对话框关闭事件处理
    const handleDialogClose = () => {
      console.log('视频对话框已关闭');
      // 保存进度
      if (videoPlayer.value) {
        const progress = Math.round((videoPlayer.value.currentTime / videoPlayer.value.duration) * 100);
        updateLearningProgress(progress, 3); // 状态为已暂停
      }
    };
    
    // 视频暂停事件处理
    const handleVideoPause = () => {
      console.log('视频已暂停');
      // 可以在这里添加暂停时的逻辑，比如记录当前时间点
      if (videoPlayer.value) {
        const currentTime = videoPlayer.value.currentTime;
        console.log(`当前播放时间: ${currentTime}秒`);
      }
    };
    
    // 视频播放事件处理
    const handleVideoPlay = () => {
      console.log('视频已开始/继续播放');
    };
    
    // 终止学习
    const stopLearning = async () => {
      if (videoPlayer.value) {
        videoPlayer.value.pause();
        
        // 保存当前进度，确保是有效的数字
        let progress = 0;
        if (videoPlayer.value.duration && !isNaN(videoPlayer.value.duration) && videoPlayer.value.duration > 0) {
          progress = Math.round((videoPlayer.value.currentTime / videoPlayer.value.duration) * 100);
          // 确保进度是有效的数字
          if (isNaN(progress)) progress = 0;
        }
        
        await updateLearningProgress(progress, 3); // 状态为已暂停
        
        // 确保进度已保存后再关闭对话框
        setTimeout(() => {
          // 清理视频资源
          currentVideo.value = '';
          
          // 关闭对话框
          videoDialogVisible.value = false;
          ElMessage.info('学习已暂停，进度已保存');
        }, 300);
      } else {
        // 如果没有视频元素，直接关闭对话框
        videoDialogVisible.value = false;
        ElMessage.info('学习已终止');
      }
    };
    
    // 播放下一章节
    const playNextChapter = () => {
      if (!hasNextChapter.value) return;
      
      // 找到当前章节的索引
      const currentIndex = chapters.value.findIndex(chapter => 
        chapter.id === currentChapter.value.id
      );
      
      // 获取下一章节
      const nextChapter = chapters.value[currentIndex + 1];
      if (nextChapter) {
        // 保存当前章节的进度
        const video = videoPlayer.value;
        if (video) {
          // 直接将当前章节标记为已完成(100%)
          updateLearningProgress(100, 2); 
        }
        
        // 播放下一章节
        playChapter(nextChapter);
      }
    };
    
    onMounted(() => {
      loadCourseDetail()
      
      // 定期更新学习进度
      progressUpdateTimer = setInterval(() => {
        if (videoPlayer.value && !videoPlayer.value.paused) {
          updateLearningProgress()
        }
      }, 30000) // 每30秒更新一次
    })

    onUnmounted(() => {
      if (progressUpdateTimer) {
        clearInterval(progressUpdateTimer)
      }
    })

    onBeforeUnmount(() => {
      if (videoPlayer.value) {
        videoPlayer.value.pause()
      }
    })
    
    return {
      loading,
      actionLoading,
      favoriteLoading,
      videoDialogVisible,
      course,
      chapters,
      learningRecord,
      activeTab,
      currentVideo,
      videoPlayer,
      relatedCourses,
      isFavorite,
      currentTime,
      duration,
      currentChapter,
      hasNextChapter,
      startLearning,
      addToFavorites,
      playChapter,
      getProgressColor,
      formatDuration,
      isChapterCompleted,
      getChapterStatusType,
      getChapterStatusText,
      handleImageError,
      formatVideoUrl,
      stopLearning,
      playNextChapter,
      // 新增的事件处理方法
      handleDialogOpen,
      handleDialogClose,
      handleVideoPause,
      handleVideoPlay,
      handleVideoProgress,
      handleVideoCompleted
    }
  }
}
</script>

<style scoped>
.course-detail {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.course-header {
  display: flex;
  gap: 30px;
  margin-bottom: 30px;
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.course-cover {
  flex-shrink: 0;
  width: 300px;
  height: 200px;
}

.cover-image {
  width: 100%;
  height: 100%;
  border-radius: 8px;
}

.no-cover {
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  border: 2px dashed #dcdfe6;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.course-info {
  flex: 1;
}

.course-title {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin: 0 0 20px 0;
}

.course-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 20px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #606266;
  font-size: 14px;
}

.course-price {
  margin: 20px 0;
}

.price {
  font-size: 24px;
  font-weight: bold;
  color: #f56c6c;
}

.course-free {
  margin: 20px 0;
}

.course-actions {
  display: flex;
  gap: 15px;
  margin: 20px 0;
}

.learning-progress {
  margin-top: 20px;
}

.progress-text {
  margin-bottom: 10px;
  font-size: 14px;
  color: #606266;
}

.course-content {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.course-description h3,
.course-chapters h3,
.course-reviews h3 {
  margin: 0 0 20px 0;
  font-size: 20px;
  color: #303133;
}

.description-content {
  line-height: 1.8;
  color: #606266;
}

.no-chapters,
.no-reviews {
  text-align: center;
  color: #909399;
  padding: 40px 0;
}

.chapter-list {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow: hidden;
}

.chapter-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
  cursor: pointer;
  transition: background-color 0.3s;
}

.chapter-item:last-child {
  border-bottom: none;
}

.chapter-item:hover {
  background-color: #f5f7fa;
}

.chapter-number {
  width: 40px;
  height: 40px;
  background: #409eff;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-right: 15px;
}

.chapter-info {
  flex: 1;
}

.chapter-title {
  font-size: 16px;
  color: #303133;
  margin-bottom: 5px;
}

.chapter-duration {
  font-size: 14px;
  color: #909399;
}

.chapter-status {
  color: #67c23a;
}

.video-player-container {
  width: 100%;
  height: calc(100vh - 120px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: #000;
}

.video-player {
  width: 100%;
  max-height: 100%;
}

.no-video {
  color: white;
  font-size: 18px;
}

.video-controls {
  display: flex;
  justify-content: space-between;
  padding: 10px;
}

@media (max-width: 768px) {
  .course-header {
    flex-direction: column;
    padding: 20px;
  }
  
  .course-cover {
    width: 100%;
    height: 200px;
  }
  
  .course-actions {
    flex-direction: column;
  }
  
  .course-meta {
    flex-direction: column;
    gap: 10px;
  }
}
</style> 