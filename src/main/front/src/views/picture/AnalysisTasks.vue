<template>
  <a-card :bordered="false">
    <!--操作按钮-->
    <div>
      <a-row :gutter="0">
        <a-col :md="14" :sm="24">
          <a-form layout="inline">
            <a-form-item label="任务名称">
              <a-input v-model="queryParam.filterName" placeholder="任务名称"/>
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
      :data-source="dataSource"
      :pagination="false"
      rowKey="name"
      size="middle">
      <a-table-column key="name" title="任务名称" data-index="name" >
        <template slot-scope="text, record">
          <a @click="onClickAnalysisResult(record.name)" style="padding-right: 50px;font-size: 16px;">
            <a-icon type="folder" style="color: goldenrod;fontSize: 20px;margin-right: 10px;" theme="filled" /><span style="color: black">{{ record.name }}</span>
          </a>
        </template>
      </a-table-column>
      <a-table-column key="lastModified" title="修改时间" data-index="lastModified" >
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
  </a-card>
</template>
<script>
import { remove, getTaskList } from '@/api/analysisTasks.js'
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
    formatDate (value) {
      return Moment(value).format('YYYY-MM-DD HH:mm:ss')
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
    deleteFile (record) {
      const requestParameters = { id: record.id }
      remove(requestParameters)
        .then(res => {
          this.loadData()
        })
    },
    loadData (callback) {
      const requestParameters = Object.assign({}, this.queryParam)
      getTaskList(requestParameters)
        .then(res => {
          this.dataSource = res.data
          if (callback) {
            callback()
          }
        })
    },
    onClickAnalysisResult (taskName) {
      this.$router.push({ path: '/task/' + taskName })
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
