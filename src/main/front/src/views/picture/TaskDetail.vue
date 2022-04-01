<template>
  <a-card :bordered="false">
    <!--操作按钮-->
    <div>
      <a-row :gutter="0">
        <a-col :md="14" :sm="24">
          <a-form layout="inline">
            <a-form-item label="文件名称">
              <a-input v-model="queryParam.name" placeholder="文件名称"/>
            </a-form-item>
            <a-form-item>
              <a-button @click="loadData()">查询</a-button>
              <a-button style="margin-left: 8px" @click="() => this.queryParam = {}">重置</a-button>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </div>
    <!--文件列表-->
    <a-table
      :bordered="true"
      :data-source="dataSource"
      :rowSelection="rowSelection"
      :pagination="false"
      rowKey="name"
      size="middle">
      <a-table-column key="name" title="文件名" data-index="name" >
        <template slot-scope="text, record">
          <a @click="onClickImage(record.name, record.resourcePath, record.canonicalFilePath)" style="padding-right: 50px;font-size: 16px;">
            <a-icon type="file-text" style="fontSize: 20px;margin-right: 10px;" theme="filled" /><span style="color: black">{{ record.name }}</span>
          </a>
        </template>
      </a-table-column>
      <a-table-column key="createDate" title="修改时间" data-index="lastModified" >
        <template slot-scope="text, record">
          {{ formatDate(record.lastModified) }}
        </template>
      </a-table-column>
      <a-table-column key="action" title="操作">
        <template slot-scope="text, record">
          <span>
            <a-button @click="deleteFile(record)" >删除</a-button>
            <a-button @click="onClickAnalysisResult(record)" type="primary" style="margin-left: 5px;">分析结果</a-button>
          </span>
        </template>
      </a-table-column>
    </a-table>
    <!--图片详情-->
    <a-drawer
      :title="image.name"
      placement="left"
      :closable="true"
      :maskClosable="false"
      :visible="image.showModal"
      width="90%"
      @close="handleImageModalCancel"
    >
      <a :href="image.resourcePath" :download="image.name" style="font-size: 20px;border: silver 1px;">下载</a>
      <span style="margin-left: 20px;">文件路径 {{ image.canonicalFilePath }}</span>
      <img :src="image.resourcePath" width="100%"/>
    </a-drawer>
  </a-card>
</template>
<script>
import { getImageList, addFolder, removeFile } from '@/api/sourceImage'
import { startTask, listAnalysisResult } from '@/api/analysis'
import { message } from 'ant-design-vue'
import Moment from 'moment'
export default {
  name: 'TableList',
  data () {
    return {
      collapseActiveKey: ['1'],
      fileList: [],
      image: {
        name: '',
        showModal: false,
        scr: ''
      },
      folder: {
        name: '',
        showModal: false
      },
      upload: {
        showModal: false
      },
      breadcrumb: [
        {
          name: '全部任务',
          id: ''
        }
      ],
      confirmLoading: false,
      // 查询参数
      queryParam: {
        filterName: ''
      },
      dataSource: []
    }
  },
  filters: {
  },
  created () {
    this.loadData()
  },
  computed: {
    rowSelection () {
      return {
        onChange: this.onSelectChange
      }
    }
  },
  methods: {
    str2JSON (str) {
      return JSON.parse(str)
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    formatDate (value) {
      return Moment(value).format('YYYY-MM-DD HH:mm:ss')
    },
    showUploadModal () {
      this.upload.showModal = true
    },
    handleUploadModalOk () {
      this.loadData()
      this.upload.showModal = false
    },
    handleUploadModalCancel () {
      this.loadData()
      this.upload.showModal = false
    },
    getUploadData () {
      return {
        folderName: this.$route.params.folderName
      }
    },
    handleChange (info) {
      if (info.file.status !== 'uploading') {
        console.log(info.file, info.fileList)
      }
      if (info.file.status === 'done') {
        message.success(`${info.file.name} file uploaded successfully`)
      } else if (info.file.status === 'error') {
        message.error(`${info.file.name} file upload failed.`)
      }
    },
    handleFolderModalCancel () {
      this.folder.showModal = false
    },
    handleFolderModalOk () {
      const requestParameters = { name: this.folder.name, parentPath: this.queryParam.parentPath }
      addFolder(requestParameters)
        .then(res => {
          this.loadData()
          this.folder.showModal = false
        })
    },
    clickAddFolder () {
      this.folder.name = ''
      this.folder.showModal = true
    },
    onClickBreadcrumb (item) {
      this.queryParam.parentPath = item.id
      for (var i = 0; i < this.breadcrumb.length; i++) {
        if (this.breadcrumb[i].id === item.id) {
          this.breadcrumb.splice(i + 1, this.breadcrumb.length)
          break
        }
      }
      this.loadData()
    },
    onClickImage (name, resourcePath, canonicalFilePath) {
      this.image.name = name
      this.image.resourcePath = resourcePath
      this.image.canonicalFilePath = canonicalFilePath
      this.image.showModal = true
    },
    handleImageModalCancel (e) {
      this.image.showModal = false
    },
    deleteFile (record) {
      const requestParameters = { id: record.id }
      removeFile(requestParameters)
        .then(res => {
          this.loadData()
        })
    },
    onClickAnalysis (record) {
      if (record) {
        this.analysisModal.formInitParam.imageList = [record.name]
      } else {
        if (!this.selectedRowKeys || this.selectedRowKeys.length === 0) {
          this.messageConfirm('请选择')
          return
        }
        this.analysisModal.formInitParam.imageList = this.selectedRowKeys
      }
      this.analysisModal.formInitParam.folderName = this.$route.params.folderName
      this.analysisModal.showModal = true
      this.resetAnalysisForm()
    },
    onClickAnalysisResult (record) {
      this.analysisResult.showModal = true
      this.loadAnalysisResult(record)
    },
    loadAnalysisResult (record) {
      const requestParameters = { folderName: this.$route.params.folderName, imageName: record.name }
      listAnalysisResult(requestParameters)
        .then(res => {
          if (res.code === 0) {
            this.analysisResult.result = res.data
          } else {
            this.$message.error('系统异常，请联系管理员！')
          }
        })
    },
    getImagePath (item) {
      return '/api' + item.path
    },
    getAnalysisResultItemTitle (item) {
      return '任务名称：' + item.taskName
    },
    onClickAnalysisModalCancel () {
      this.analysisModal.showModal = false
    },
    /** 执行分析 */
    onClickAnalysisModalOk () {
      const that = this
      this.analysisForm.validateFields(function (errors, values) {
        if (errors) {
          return false
        } else {
          const taskName = values.name
          const folderName = that.analysisModal.formInitParam.folderName
          const imageList = that.analysisModal.formInitParam.imageList
          delete values.name
          const requestParameters = { taskName: taskName, folderName: folderName, imageList: JSON.stringify(imageList), param: JSON.stringify(values) }
          startTask(requestParameters)
            .then(res => {
              if (res.code === 0) {
                that.onClickAnalysisModalCancel()
                that.messageConfirm('任务正在执行中，请稍后查看分析结果')
              } else {
                that.$message.error('任务执行失败，请重试！或请联系管理员！')
              }
            })
        }
      })
    },
    resetAnalysisForm (param) {
      if (!param) {
        param = this.analysisModal.formInitParam
      }
      this.$nextTick(() => {
        this.analysisForm.setFieldsValue(param)
      })
    },
    messageConfirm (msg) {
      const h = this.$createElement
      this.$success({
        title: '提示',
        content: h('div', {}, [
          h('p', msg)
        ]),
        onOk () {

        }
      })
    },
    messageError (msg) {
      const h = this.$createElement
      this.$error({
        title: '异常',
        content: h('div', {}, [
          h('p', msg)
        ]),
        onOk () {

        }
      })
    },
    loadData (callback) {
      const requestParameters = Object.assign({ folderName: this.$route.params.folderName }, this.queryParam)
      getImageList(requestParameters)
        .then(res => {
          this.dataSource = res.data
          if (callback) {
            callback()
          }
        })
    },
    handleAnalysisResultModalClose () {
      this.analysisResult.showModal = false
    }
  }
}
</script>
<style scoped lang="less">
.sample-table {
  td {
    text-align: left;
    padding: 5px;
  }
}
</style>
