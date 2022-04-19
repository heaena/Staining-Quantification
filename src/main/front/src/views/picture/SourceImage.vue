<template>
  <a-card :bordered="false">
    <!--操作按钮-->
    <div>
      <a-row :gutter="0">
        <a-col :md="10" :sm="24">
          <a-form layout="inline">
            <a-form-item>
              <a-button icon="folder-add" @click="showUploadModal()" type="primary">Upload Image</a-button>
              <a-button icon="folder-add" @click="clickAddFolder()">Create New Folder</a-button>
            </a-form-item>
          </a-form>
        </a-col>
        <a-col :md="14" :sm="24">
          <a-form layout="inline">
            <a-form-item label="File Name">
              <a-input v-model="queryParam.name" placeholder="File Name"/>
            </a-form-item>
            <a-form-item>
              <a-button @click="loadData()">Search</a-button>
              <a-button style="margin-left: 8px" @click="() => this.queryParam = {}">Reset</a-button>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </div>
    <!--文件导航-->
    <a-breadcrumb separator="/">
      <a-breadcrumb-item v-for="(item, index) in breadcrumb" :key="item.id">
        <a v-if="index !== breadcrumb.length - 1" @click="onClickBreadcrumb(item)">{{ item.name }}</a>
        <span v-else>{{ item.name }}</span>
      </a-breadcrumb-item>
    </a-breadcrumb>
    <!--文件列表-->
    <a-table :data-source="dataSource" rowKey="path">
      <a-table-column key="name" title="File Name" data-index="name" >
        <template slot-scope="text, record">
          <a @click="onClickItem(record)">
            <a-icon v-if="record.dir===true" type="folder" style="color: goldenrod;fontSize: 20px;margin-right: 10px;" theme="filled" />
            <a-icon v-else type="file-text" style="fontSize: 20px;margin-right: 10px;" theme="filled" /><span style="color: black">{{ record.name }}</span>
          </a>
        </template>
      </a-table-column>
      <a-table-column key="lastModified" title="Last Edited Time" data-index="lastModified" >
        <template slot-scope="text, record">
          {{ formatDate(record.lastModified) }}
        </template>
      </a-table-column>
      <a-table-column key="action" title="Action">
        <template slot-scope="text, record">
          <span>
            <a-button @click="deleteFile(record)" >Delete</a-button>
            <a-button @click="onClickAnalysis(record)" type="primary" style="margin-left: 5px;">Create Analysis Task</a-button>
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
      <a :href="image.resourcePath" :download="image.name" style="font-size: 20px;border: silver 1px;">Download</a>
      <span style="margin-left: 20px;">Image path： {{ image.canonicalFilePath }}</span>
      <img :src="image.resourcePath" width="100%"/>
    </a-drawer>
    <!--新增文件夹-->
    <a-modal
      title="Create New Folder"
      :visible="folder.showModal"
      @cancel="handleFolderModalCancel"
      @ok="handleFolderModalOk"
      okText="OK"
      cancelText="Cancel"
    >
      <a-input placeholder="Folder Name" v-model="folder.name"/>
    </a-modal>
    <!--上传图片-->
    <a-modal
      title="Upload Image"
      :visible="upload.showModal"
      @cancel="handleUploadModalCancel"
      @ok="handleUploadModalOk"
      okText="OK"
      cancelText="Cancel"
    >
      <a-upload
        v-model="upload.fileList"
        name="files"
        accept="image/*"
        :multiple="true"
        :withCredentials="true"
        listType="picture"
        action="/api/sourceImage/uploadFile"
        :headers="headers"
        :data="getUploadData()"
        @change="handleChange"
        :showUploadList="upload.showUploadList"
      >
        <a-button type="primary">
          <a-icon type="upload" />
          Choose Image
        </a-button>
      </a-upload>
    </a-modal>
    <!--分析-->
    <a-modal
      title="Create Analysis Task"
      :visible="analysisModal.showModal"
      @cancel="onClickAnalysisModalCancel"
      @ok="onClickAnalysisModalOk"
      okText="OK"
      cancelText="Cancel"
    >
      <a-form :form="analysisForm" :label-col="{ span: 7 }" :wrapper-col="{ span: 16 }">
        <a-form-item label="Task Title">
          <a-input
            placeholder="Mark this task"
            :maxLength="50"
            v-decorator="['taskName', { rules: [{ required: true, message: 'Please input!' }] }]"
          />
        </a-form-item>
        <a-form-item label="local density filter" extra="note: denosing parameter. default value is 0. The most commonly used range of this parameter is 0~0.05.">
          <a-input-number
            :min="0"
            placeholder="denosing parameter"
            style="width: 100%"
            v-decorator="['d-thr', {initialValue: '0.01'}, { rules: [{ required: true, message: 'Please select!' }] }]"
          />
        </a-form-item>
        <a-form-item label="fill" extra="note: dilate parameter. default value is 10. The most commonly used range is 0~15">
          <a-input-number
            :min="0"
            placeholder="dilate parameter"
            style="width: 100%"
            v-decorator="['fill', {initialValue: '10'}, { rules: [{ required: true, message: 'Please select!' }] }]"
          />
        </a-form-item>
        <a-form-item label="flood" extra="note: default value is Yes. whether to flood the connected areas">
          <a-select
            v-decorator="[
              'flood', {initialValue: 'Y'},
              { rules: [{ required: true, message: 'Please select!' }] },
            ]"
            placeholder="whether to flood the connected areas"
          >
            <a-select-option value="Y">
              Yes
            </a-select-option>
            <a-select-option value="N">
              No
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="object area excluded" extra="note: denosing parameter. default value is 5%. The most commonly used range is 0~10%.">
          <a-input-number
            :min="0"
            :max="100"
            placeholder="denosing parameter"
            style="width: 100%"
            :formatter="value => `${value}%`"
            :parser="value => value.replace('%', '')"
            v-decorator="['obj-thr', {initialValue: '5'}, { rules: [{ required: true, message: 'Please select!' }] }]"
          />
        </a-form-item>
        <a-form-item label="staining threshold" extra="note: the staining threshold paramter ranges from 0~255. the default for Von Kossa staining is set to 140, and for Alizarin Red is set to 100.">
          <a-input-number
            :min="0"
            :max="255"
            placeholder="the staining threshold paramter"
            style="width: 100%"
            v-decorator="['stained-thr', {initialValue: '100'}, { rules: [{ required: true, message: 'Please select!' }] }]"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>
<script>
import { getList, addFolder, removeFile, createTask } from '@/api/sourceImage'
import { message, Modal } from 'ant-design-vue'
import Moment from 'moment'
export default {
  name: 'TableList',
  data () {
    return {
      analysisForm: {},
      headers: {
        authorization: 'authorization-text'
      },
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
        showModal: false,
        showUploadList: {
          showRemoveIcon: false
        },
        fileList: []
      },
      breadcrumb: [
        {
          name: 'All Files',
          path: ''
        }
      ],
      // create model
      visible: false,
      confirmLoading: false,
      // 查询参数
      queryParam: {
        parentPath: '',
        name: ''
      },
      dataSource: [],
      analysisModal: {
        showModal: false,
        param: {
          path: ''
        }
      }
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
        selectedRowKeys: this.selectedRowKeys,
        onChange: this.onSelectChange
      }
    }
  },
  methods: {
    formatDate (value) {
      return Moment(value).format('YYYY-MM-DD HH:mm:ss')
    },
    showUploadModal () {
      console.log('-----')
      this.upload.fileList = []
      this.upload.showModal = true
      console.log(this.upload.fileList)
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
        parentPath: this.queryParam.parentPath
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
      this.queryParam.parentPath = item.path
      for (var i = 0; i < this.breadcrumb.length; i++) {
        if (this.breadcrumb[i].path === item.path) {
          this.breadcrumb.splice(i + 1, this.breadcrumb.length)
          break
        }
      }
      this.loadData()
    },
    onClickItem (record) {
      if (record.dir === true) {
        this.breadcrumb.push(record)
        this.dataSource = []
        this.queryParam.parentPath = record.path
        this.loadData()
      } else {
        this.image.name = record.name
        this.image.resourcePath = record.resourcePath
        this.image.canonicalFilePath = record.canonicalFilePath
        this.image.showModal = true
      }
    },
    handleImageModalCancel (e) {
      this.image.showModal = false
    },
    deleteFile (record) {
      const requestParameters = { path: record.path }
      removeFile(requestParameters)
        .then(res => {
          this.loadData()
        })
    },
    onClickAnalysis (record) {
      this.analysisModal.param.path = record.path
      this.analysisModal.showModal = true
      this.analysisForm = this.$form.createForm(this, { name: 'coordinated' })
    },
    getImagePath (item) {
      return '/api' + item.path
    },
    onClickAnalysisModalCancel () {
      this.analysisModal.showModal = false
    },
    onClickAnalysisModalOk () {
      const that = this
      this.analysisForm.validateFields(function (errors, values) {
        if (errors) {
          return false
        } else {
          const taskName = values.taskName
          const path = that.analysisModal.param.path
          delete values.taskName
          delete values.path
          const requestParameters = { path: path, taskName: taskName, param: JSON.stringify(values) }
          createTask(requestParameters)
            .then(res => {
              if (res.code === 0) {
                that.onClickAnalysisModalCancel()
                that.messageConfirm('The task is in progress. Please check the analysis results later', res.data)
              } else {
                that.warning(res.msg)
              }
            })
        }
      })
    },
    messageConfirm (msg, taskName) {
      const that = this
      if (taskName) {
        Modal.confirm({
          title: 'Success',
          content: msg,
          okText: 'View Analysis Results',
          closable: true,
          closeIcon: <close-outlined />,
          maskClosable: true,
          cancelText: 'Got it',
          icon: function () {
            return ''
          },
          onOk () {
            that.$router.push({ path: '/task/' + taskName })
          }
        })
      } else {
        Modal.confirm({
          title: 'Create failed ！',
          closable: true,
          okText: 'Got it',
          maskClosable: true,
          icon: function () {
            return ''
          }
        })
      }
    },
    loadData (callback) {
      const requestParameters = Object.assign({}, this.queryParam)
      getList(requestParameters)
        .then(res => {
          this.dataSource = res.data
          if (callback) {
            callback()
          }
        })
    }
  }
}
</script>
