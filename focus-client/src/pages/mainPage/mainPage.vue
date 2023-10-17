<template>
  <div id="box">
    <el-container>
      <el-header class="header">
        <el-input v-model="token" placeholder="Token" class="tokenInput"></el-input>
        <div class="statusBox">
          <el-text class="mx-1">当前：</el-text>
          <el-text class="mx-1" type="success">{{linkStatus}}</el-text>
        </div>
        <el-input v-model="serverAddress" placeholder="服务器地址" class="serverInput">
          <template #append>
            <el-button @click="conn" :icon="Connection" title="保存并连接" />
          </template>
        </el-input>
      </el-header>
      <el-main>
        <el-empty description="暂无数据" v-if="dataList.length <1" />
        <el-card class="box-card" v-for="data in dataList" :key="data.id">
          <template #header>
            <div class="card-header">
              <span>{{data.phone}}</span>
            </div>
          </template>
          <div class="timeBox">{{data.time}}</div>
          <div class="contentBox">{{data.content}}</div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { Connection } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
const { ipcRenderer } = require("electron");
let linkStatus = ref("未连接");
let token = ref(localStorage.getItem("token"));
let serverAddress = ref(localStorage.getItem("serverAddress"));
let websocket;
connWeb();
let conn = () => {
  localStorage.setItem("token", token.value);
  localStorage.setItem("serverAddress", serverAddress.value);
  try {
    websocket.close();
  } catch (e) {
    console.log(e);
  }
  linkStatus.value = "未连接";
  connWeb();
};
let dataList = ref([]);
function connWeb() {
  try {
    websocket = new WebSocket(serverAddress.value + "/" + token.value);
    websocket.onopen = event => {
      let notice = {
        title: "已启动",
        subtitle: "推送服务启动成功",
        body: "已连接到:" + serverAddress.value
      };
      ipcRenderer.send("notice", notice);
      linkStatus.value = "已连接！";
    };
    // 监听服务器端返回的信息
    websocket.onmessage = event => {
      console.log(event.data);
      let json = JSON.parse(event.data);
      let notice = {
        title: json.phone,
        subtitle: json.time,
        body: json.content
      };
      ipcRenderer.send("notice", notice);
      let msg = {
        phone: json.phone,
        time: json.time,
        content: json.content
      };
      dataList.value.push(msg);
    };
    websocket.onerror = event => {
      let notice = {
        title: "启动失败",
        subtitle: "推送服务启动失败",
        body: "无法连接到:" + serverAddress.value
      };
      ipcRenderer.send("notice", notice);
    };
  } catch (e) {
    let notice = {
      title: "启动失败",
      subtitle: "推送服务启动失败",
      body: "无法连接到:" + serverAddress.value
    };
    ipcRenderer.send("notice", notice);
  }
}
</script>

<style>
.statusBox {
  float: right;
  margin-right: 35px;
  line-height: 30px;
}
.serverInput {
  margin-top: 10px;
  float: left;
}
.tokenInput {
  width: 200px;
}
.timeBox {
  font-size: 13px;
}
#box {
  width: 100vw;
  height: 100vh;
  /* overflow: hidden; */
  padding-top: 10px;
  padding-bottom: 10px;
}

.box-card {
  /* width: 96%; */
  margin: 0px auto;
  border-radius: 20px;
  margin-top: 5px;
}
</style>