<template>
  <a-card :bordered="false">
    <!--操作按钮-->
    <div>
      <a-row :gutter="0">
        <a-col :md="10" :sm="24">
          <a-form layout="inline">
            <a-form-item>
              <a-button type="primary" icon="folder-add" @click="clickAddFolder()">新建文件夹</a-button>
            </a-form-item>
          </a-form>
        </a-col>
        <a-col :md="14" :sm="24">
          <a-form layout="inline">
            <a-form-item label="文件夹名称">
              <a-input v-model="queryParam.name" placeholder="文件夹名称"/>
            </a-form-item>
            <a-form-item>
              <a-button @click="loadData()">查询</a-button>
              <a-button style="margin-left: 8px" @click="() => this.queryParam = {}">重置</a-button>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </div>
    <!--文件夹列表-->
    <a-table :data-source="dataSource" size="middle" :pagination="false">
      <a-table-column key="name" title="文件夹名称" data-index="name" >
        <template slot-scope="text, record">
          <a @click="enterFolder(record)" style="padding-right: 50px;font-size: 16px;">
            <a-icon type="folder" style="color: goldenrod;padding-right: 10px;" theme="filled" />
            {{ record.name }}
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
            <a-button @click="deleteFolder(record)">删除</a-button>
            <a-button type="primary" @click="enterFolder(record)" style="margin-left: 10px;">进入</a-button>
          </span>
        </template>
      </a-table-column>
    </a-table>
    <!--新增文件夹-->
    <a-modal
      title="新建文件夹"
      :visible="folder.showModal"
      @cancel="handleFolderModalCancel"
      @ok="handleFolderModalOk"
    >
      <a-input placeholder="文件夹名称" v-model="folder.name"/>
    </a-modal>
  </a-card>
</template>
<script>
import { getFolderList, addFolder, removeFile } from '@/api/fileSystem'
import Moment from 'moment'
export default {
  name: 'TableList',
  data () {
    return {
      analysisForm: this.$form.createForm(this, { name: 'coordinated' }),
      fileList: [],
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
        showModal: false
      },
      breadcrumb: [
        {
          name: '全部文件',
          id: ''
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
        formInitParam: {
          name: '',
          roi_fill_thr: 7,
          stained_thr: 'auto'
        }
      },
      analysisResult: {
        showModal: false,
        fileId: '',
        showId: ['1'],
        analysisResultDataSource: []
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
    deleteFolder (record) {
      const requestParameters = { name: record.name }
      removeFile(requestParameters)
        .then(res => {
          this.loadData()
        })
    },
    enterFolder (record) {
      this.$router.push({
        path: '/images/' + record.name
      })
    },
    loadData (callback) {
      const requestParameters = Object.assign({}, this.queryParam)
      getFolderList(requestParameters)
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
