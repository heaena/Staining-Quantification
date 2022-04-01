<template>
  <a-card :bordered="false">
    <!--操作按钮-->
    <div>
      <a-row :gutter="0">
        <a-col :md="10" :sm="24">
          <a-form layout="inline">
            <a-form-item>
              <a-button icon="folder-add" @click="showUploadModal()" type="primary">上传图片</a-button>
              <a-button icon="folder-add" @click="clickAddFolder()">新建文件夹</a-button>
            </a-form-item>
          </a-form>
        </a-col>
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
    <!--文件导航-->
    <a-breadcrumb separator="/">
      <a-breadcrumb-item v-for="(item, index) in breadcrumb" :key="item.id">
        <a v-if="index !== breadcrumb.length - 1" @click="onClickBreadcrumb(item)">{{ item.name }}</a>
        <span v-else>{{ item.name }}</span>
      </a-breadcrumb-item>
    </a-breadcrumb>
    <!--文件列表-->
    <a-table :data-source="dataSource" rowKey="path">
      <a-table-column key="name" title="文件名" data-index="name" >
        <template slot-scope="text, record">
          <a @click="onClickItem(record)">
            <a-icon v-if="record.dir===true" type="folder" style="color: goldenrod;fontSize: 20px;margin-right: 10px;" theme="filled" />
            <a-icon v-else type="file-text" style="fontSize: 20px;margin-right: 10px;" theme="filled" /><span style="color: black">{{ record.name }}</span>
          </a>
        </template>
      </a-table-column>
      <a-table-column key="createDate" title="修改时间" data-index="createDate" />
      <a-table-column key="action" title="操作">
        <template slot-scope="text, record">
          <span>
            <a-button @click="deleteFile(record)" >删除</a-button>
            <a-button @click="onClickAnalysis(record)" type="primary" style="margin-left: 5px;">创建分析任务</a-button>
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
    <!--新增文件夹-->
    <a-modal
      title="新建文件夹"
      :visible="folder.showModal"
      @cancel="handleFolderModalCancel"
      @ok="handleFolderModalOk"
    >
      <a-input placeholder="文件夹名称" v-model="folder.name"/>
    </a-modal>
    <!--上传图片-->
    <a-modal
      title="上传图片"
      :visible="upload.showModal"
      @cancel="handleUploadModalCancel"
      @ok="handleUploadModalOk"
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
          选择图片
        </a-button>
      </a-upload>
    </a-modal>
    <!--分析-->
    <a-modal
      title="创建分析任务"
      :visible="analysisModal.showModal"
      @cancel="onClickAnalysisModalCancel"
      @ok="onClickAnalysisModalOk"
      okText="确定"
    >
      <a-form :form="analysisForm" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
        <a-form-item label="任务名称">
          <a-input
            placeholder="标注此次任务"
            :maxLength="50"
            v-decorator="['taskName', { rules: [{ required: true, message: 'Please input!' }] }]"
          />
        </a-form-item>
        <a-form-item label="d.thr">
          <a-input-number
            :min="0"
            placeholder="初次降噪"
            style="width: 100%"
            v-decorator="['d.thr', {initialValue: '0'}, { rules: [{ required: true, message: 'Please select!' }] }]"
          />
          常用范围为0-0.05
        </a-form-item>
        <a-form-item label="fill">
          <a-input-number
            :min="0"
            placeholder="加深初次识别区域"
            style="width: 100%"
            v-decorator="['fill', {initialValue: '10'}, { rules: [{ required: true, message: 'Please select!' }] }]"
          />
          常用范围为0-15
        </a-form-item>
        <a-form-item label="flood">
          <a-select
            v-decorator="[
              'flood', {initialValue: 'Y'},
              { rules: [{ required: true, message: 'Please select!' }] },
            ]"
            placeholder="将相连区域填实"
          >
            <a-select-option value="Y">
              Yes
            </a-select-option>
            <a-select-option value="N">
              No
            </a-select-option>
          </a-select>
          <span>选择相连区域是否需要填实</span>
        </a-form-item>
        <a-form-item label="obj.thr">
          <a-input-number
            :min="0"
            :max="100"
            placeholder="最终降噪"
            style="width: 100%"
            :formatter="value => `${value}%`"
            :parser="value => value.replace('%', '')"
            v-decorator="['obj.thr', {initialValue: '5'}, { rules: [{ required: true, message: 'Please select!' }] }]"
          />
          去掉面积占全部图片的百分比，常用范围为0-10
        </a-form-item>
        <a-form-item label="stained.thr">
          <a-input-number
            :min="0"
            :max="255"
            placeholder="染色区域阈值"
            style="width: 100%"
            v-decorator="['obj.thr', {initialValue: '100'}, { rules: [{ required: true, message: 'Please select!' }] }]"
          />
          von kossa常用值为140，alizarin red常用值为100
        </a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>
<script>
import { getList, addFolder, removeFile, createTask } from '@/api/fileSystem'
import { message } from 'ant-design-vue'
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
          name: '全部文件',
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
        folderId: this.queryParam.parentPath
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
          debugger
          createTask(requestParameters)
            .then(res => {
              if (res.code === 0) {
                that.onClickAnalysisModalCancel()
                that.messageConfirm('任务执行中，请稍后查看分析结果')
              } else {
                that.warning(res.msg)
              }
            })
        }
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
