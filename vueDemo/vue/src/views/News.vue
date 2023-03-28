<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <!--      顶行 筛选栏 两个框-->
      <el-form ref="form" :model="form" label-width="120px" :rules="rules">
        <el-row>
          <el-form-item prop="eventProject">
            <span style="font-size: larger">赛事项目:</span>
            <el-select
              v-model="form.eventProject"
              placeholder="赛事项目"
              filterable
              clearable
              style="margin: 10px 10px 10px 0"
              @change="queryProject"
            >
              <el-option v-for="(item, i) in projectOptions" :key="i" :label="item.projectName" :value="item.projectName"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="eventId">
            <span style="font-size: larger">赛事名称:</span>
            <el-select v-model="form.eventId" placeholder="赛事名称" filterable clearable style="margin: 10px 10px 10px 0">
              <el-option v-for="(item, i) in events" :key="i" :label="item.eventName" :value="item.id"> </el-option>
            </el-select>
          </el-form-item>
        </el-row>
      </el-form>
    </div>
    <!--    主体区域 分两块-->
    <div style="height: 200px; width: 99%">
      <div style="text-align: center; background-color: #cccccc; height: 30px; font-weight: bolder; line-height: 30px">赛事提醒</div>
      <el-input type="textarea" v-model="form.mailText"></el-input>
      <el-upload
        class="upload-demo"
        :limit="1"
        ref="upload"
        action="https://jsonplaceholder.typicode.com/posts/"
        :file-list="form.file"
        :on-change="getFile"
        :auto-upload="false"
      >
        <el-button slot="trigger" size="small" type="primary" style="margin-top: 8px; font-size: 14px">选取附件</el-button>
        <div slot="tip" class="el-upload__tip" style="font-size: 14px">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
      <el-button type="success" @click="sendMail" style="font-size: 14px" v-loading.fullscreen.lock="fullscreenLoading">发布</el-button>
    </div>
    <div style="height: 30%; width: 99%">
      <div style="text-align: center; background-color: #cccccc; height: 30px; font-weight: bolder; line-height: 30px">赛事公告</div>
      <el-input type="button" model-value="添加公告" @click="dialogVisible = true"></el-input>
    </div>
    <el-dialog v-model="dialogVisible" title="Tips" width="30%">
      <el-form label-position="right" label-width="100px" :model="dynamicForm" style="max-width: 460px">
        <el-row v-for="(item, index) in dynamicForm.announcement" :key="index">
          <el-col :span="10">
            <el-form-item
              label="名称"
              :prop="'announcement.' + index + '.name'"
              :rules="{
                required: true,
                message: '公告名称不能为空',
                trigger: 'blur',
              }"
            >
              <el-input v-model="item.name" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item
              label="描述"
              :prop="'announcement.' + index + '.description'"
              :rules="{
                required: true,
                message: '描述不能为空',
                trigger: 'blur',
              }"
            >
              <el-input v-model="item.description" />
            </el-form-item>
          </el-col>
          <el-col :span="2"> <el-button type="danger">添加</el-button></el-col>
          <el-col :span="2"> <el-button type="danger">删除</el-button></el-col>
        </el-row>
        <!-- <el-form-item label="Name">
          <el-input v-model="formLabelAlign.name" />
        </el-form-item>
        <el-form-item label="Activity zone">
          <el-input v-model="formLabelAlign.region" />
        </el-form-item>
        <el-form-item label="Activity form">
          <el-input v-model="formLabelAlign.type" />
        </el-form-item> -->
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="dialogVisible = false"> 确定 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from '../utils/request';
import E from 'wangeditor';
let editor;
export default {
  name: 'News',
  components: {},
  data() {
    return {
      form: {},
      dialogVisible: false,
      vis: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      search: '',
      detail: {},
      fullscreenLoading: false,
      events: [],
      rules: {
        eventProject: [
          { required: true, message: '请选择赛事项目', trigger: 'blur' },
          { min: 1, max: 10, message: '赛事项目不能为空', trigger: 'blur' },
        ],
        eventId: [
          { required: true, message: '请选择赛事', trigger: 'blur' },
          { min: 1, max: 32, message: '赛事不能为空', trigger: 'blur' },
        ],
      },
      file: null,
      projectOptions: [],
      // filesUploadUrls: "http://" + window.server.filesUploadUrl + ":9090/files/upload",
      tableData: [],
      dynamicForm: {
        announcement: [
          {
            name: '',
            description: '',
          },
        ],
      },
    };
  },
  created() {
    // 页面加载时就执行的方法
    this.load();
  },
  methods: {
    details(row) {
      this.vis = true;
      this.detail = row;
    },
    getFile(file, fileList) {
      this.file = file.raw;
    },
    createEditor() {
      editor = new E('#div1');
      //因为这个富文本编辑器对返回的数据类型有严格要求，重新写一个接口
      editor.config.uploadImgServer = 'http://localhost:9090/files/editor/upload';
      editor.config.uploadFileName = 'file';
      editor.create();
    },
    //查询方法
    load() {
      request.post('/event/queryAllProjectName').then((res) => {
        this.projectOptions = res.data;
      });
    },
    queryProject() {
      request.post('/event/queryAllEvents', this.form).then((res) => {
        this.events = res.data.list;
      });
    },
    add() {
      this.dialogVisible = true;
      this.form = {}; //清空表单域
      this.$nextTick(() => {
        if (!editor) {
          this.createEditor();
        } else {
          editor.destroy();
          this.createEditor();
        }
      });
    },
    sendMail() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (!this.form.eventProject) {
            this.$message.error('请选择赛事项目');
            return;
          }
          if (!this.form.eventId) {
            this.$message.error('请选择赛事');
            return;
          }
        }
      });
      let params = new FormData();
      if (this.file === null) {
        this.$message({
          type: 'error',
          message: '请选择附件',
        });
      } else {
        params.append('file', this.file);
        params.append('eventId', this.form.eventId);
        params.append('mailText', this.form.mailText);
        request.post('/news/sendMail', params, {
          'Content-Type': 'multipart/form-data',
        });
        this.fullscreenLoading = true;
        setTimeout(() => {
          this.fullscreenLoading = false;
          this.$message({
            type: 'success',
            message: '发布成功',
          });
        }, 5000);
      }
    },
    //编辑
    handleClick(row) {
      this.form = JSON.parse(JSON.stringify(row)); //这一步将数据转来转去的操作能将要展示的数据搞成一个独立的对象
      //这里的form有id
      this.dialogVisible = true;
      this.$nextTick(() => {
        if (!editor) {
          this.createEditor();
          editor.txt.html(row.content);
        } else {
          editor.destroy();
          this.createEditor();
          editor.txt.html(row.content);
        }
      });
    },
    handleDelete(id) {
      console.log(id);
      request.delete('/news/' + id).then((res) => {
        console.log(res);
        if (res.code === '0') {
          //element ui 提供的提示框
          this.$message({
            type: 'success',
            message: '删除成功',
          });
        } else {
          this.$message({
            type: 'error',
            message: res.msg,
          });
        }
        this.load();
      });
    },
    handleSizeChange(ps) {
      this.pageSize = ps;
      this.load();
    },
    handleCurrentChange(pn) {
      this.currentPage = pn;
      this.load();
    },
  },
};
</script>
