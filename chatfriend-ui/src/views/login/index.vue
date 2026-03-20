<!--
  - Copyright (c) 2025 ChatFriend. Made by Akshita Sahu. All Rights Reserved.
  -
  - Licensed under the GNU Affero General Public License, Version 3 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     https://www.gnu.org/licenses/agpl-3.0.html
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<script lang="ts" setup>
  import { reactive, ref, toRaw } from 'vue';
  import { useRouter } from 'vue-router';
  import { useUserStore } from '@/store/modules/user';
  import { useMessage } from 'naive-ui';
  import { LockClosedOutline, PersonOutline } from '@vicons/ionicons5';
  import { PageEnum } from '@/enums/pageEnum';
  import { websiteConfig } from '@/config/website.config';

  const formRef = ref();
  const message = useMessage();
  const loading = ref(false);
  const userStore = useUserStore();
  const router = useRouter();
  const form = reactive({
    username: 'administrator',
    password: 'chatfriend',
  });

  const rules = {
    username: { required: true, message: '请输入用户名', trigger: 'blur' },
    password: { required: true, message: '请输入密码', trigger: 'blur' },
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    formRef.value.validate(async (errors: any) => {
      if (!errors) {
        message.loading('登录中...');
        loading.value = true;

        try {
          await userStore.login(toRaw(form));
          const toPath = decodeURIComponent(
            (router.currentRoute.value.query?.redirect || '/') as string
          );
          message.destroyAll();
          if (router.currentRoute.value.name === PageEnum.BASE_LOGIN_NAME) {
            await router.push('/');
          } else {
            await router.push(toPath);
          }
        } finally {
          loading.value = false;
        }
      }
    });
  };

  function onCodeLogin() {
    message.warning('暂时没有接入短信登录方式');
  }
</script>

<template>
  <div class="view-account flex h-screen w-full overflow-hidden font-sans">
    <!-- Left: Branding -->
    <div class="hidden md:flex flex-1 bg-gradient-to-br from-[#6C3DE0] to-[#3D8EDE] justify-center items-center text-white p-12 relative">
      <div class="absolute w-96 h-96 bg-white opacity-10 rounded-full -top-10 -left-10 blur-3xl"></div>
      <div class="absolute w-80 h-80 bg-[#3D8EDE] opacity-30 rounded-full bottom-20 right-10 blur-2xl"></div>
      
      <div class="z-10 text-center flex flex-col items-center">
        <!-- Optional logo -->
        <!-- <img :src="websiteConfig.loginImage" class="h-24 mx-auto mb-8 shadow-2xl rounded-xl" alt="Logo" v-if="websiteConfig.loginImage"/> -->
        
        <h1 class="text-6xl font-bold mb-6 tracking-tight drop-shadow-lg">ChatFriend</h1>
        <p class="text-xl opacity-90 font-light max-w-md mx-auto">{{ websiteConfig.loginDesc || 'Connect with your friends smoothly in real-time.' }}</p>
        
        <div class="mt-20 px-6 py-2 bg-white/10 backdrop-blur-md rounded-full text-sm opacity-80 border border-white/20">
          © 2025 ChatFriend. Made by Akshita Sahu.
        </div>
      </div>
    </div>

    <!-- Right: Form -->
    <div class="flex-1 flex justify-center items-center bg-white dark:bg-gray-900 transition-colors duration-300 relative">
      <!-- Mobile Background Header -->
      <div class="md:hidden absolute top-0 left-0 right-0 h-48 bg-gradient-to-br from-[#6C3DE0] to-[#3D8EDE] rounded-b-3xl z-0"></div>
      
      <div class="w-full max-w-md p-8 sm:p-10 bg-white/90 backdrop-blur-lg rounded-2xl shadow-xl md:shadow-none border border-gray-100 md:border-transparent z-10 transition-transform hover:-translate-y-1 hover:shadow-2xl duration-500">
        <div class="text-center text-3xl font-bold mb-8 text-gray-800 dark:text-gray-100">Welcome Back</div>
        <n-form ref="formRef" :model="form" :rules="rules" label-placement="left" size="large">
          <n-form-item class="login-animation1" path="username">
            <n-input v-model:value="form.username" placeholder="请输入用户名" round>
              <template #prefix>
                <n-icon color="#808695" size="18">
                  <PersonOutline />
                </n-icon>
              </template>
            </n-input>
          </n-form-item>
          <n-form-item class="login-animation2" path="password">
            <n-input
              v-model:value="form.password"
              placeholder="请输入密码"
              showPasswordOn="click"
              type="password"
              round
            >
              <template #prefix>
                <n-icon color="#808695" size="18">
                  <LockClosedOutline />
                </n-icon>
              </template>
            </n-input>
          </n-form-item>
          <n-form-item class="login-animation3 mt-4">
            <n-button :loading="loading" block size="large" type="primary" round @click="handleSubmit" color="#6C3DE0">
              登录
            </n-button>
          </n-form-item>
          <div class="login-animation4 mb-3 mt-4">
            <div class="w-full flex justify-between items-center px-2">
              <span class="text-sm text-gray-500">Don't have an account?</span>
              <n-button text type="primary" @click="router.push(PageEnum.BASE_REGISTER)" color="#6C3DE0" class="font-bold">
                注册账号
              </n-button>
            </div>
          </div>
          <div class="mt-8 text-xs text-gray-400 login-animation5 text-center">
            * 建议使用 Chrome、Edge 或 Safari 访问以获得最佳体验。
          </div>
          
          <div class="md:hidden mt-6 text-xs text-gray-400 text-center">
            © 2025 ChatFriend. Made by Akshita Sahu.
          </div>
        </n-form>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
  @keyframes anim-num {
    0% {
      transform: translateY(30px);
      opacity: 0;
    }
    100% {
      transform: translateY(0);
      opacity: 1;
    }
  }
  .login-animation1 { opacity: 0; animation: anim-num 0.5s forwards 0.1s; }
  .login-animation2 { opacity: 0; animation: anim-num 0.5s forwards 0.2s; }
  .login-animation3 { opacity: 0; animation: anim-num 0.5s forwards 0.3s; }
  .login-animation4 { opacity: 0; animation: anim-num 0.5s forwards 0.4s; }
  .login-animation5 { opacity: 0; animation: anim-num 0.5s forwards 0.5s; }
</style>
