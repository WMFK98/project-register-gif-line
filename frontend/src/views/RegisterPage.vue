<script setup>
import LogoGiffarine from './../components/icons/LogoGiffarine.vue'
import NawaImg from './../components/images/NawaphanImg.vue'
import EllipseBackground from './../components/images/EllipseBackground.vue'
import BoxText from './../components/BoxText.vue'
import InputText from './../components/InputText.vue'

import BtnForm from './../components/BtnForm.vue'
import { checkBlobURL } from './../libs/previewBinary'
import { useVuelidate } from '@vuelidate/core'
import { required, minLength, helpers, numeric } from '@vuelidate/validators'
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { toast } from 'vue3-toastify'
import { useImageStore } from '@/store/imageStore'
import axios from 'axios'

const typeAnimate = reactive({
  typeValue: '',
  typeStatus: false,
  displayTextArray: [
    'ยินดีต้อนรับสู่ การสมัครสมาชิก สมัครบัตร VIP รับส่วนลด สมัครตัวแทนสอนขายออนไลน์ฟรี'
  ],
  typingSpeed: 50,
  erasingSpeed: 100,
  newTextDelay: 1000,
  displayTextArrayIndex: 0,
  charIndex: 0
})

const typeText = () => {
  if (
    typeAnimate.charIndex < typeAnimate.displayTextArray[typeAnimate.displayTextArrayIndex].length
  ) {
    if (!typeAnimate.typeStatus) typeAnimate.typeStatus = true
    typeAnimate.typeValue += typeAnimate.displayTextArray[typeAnimate.displayTextArrayIndex].charAt(
      typeAnimate.charIndex
    )
    typeAnimate.charIndex += 1
    setTimeout(typeText, typeAnimate.typingSpeed)
  } else {
    typeAnimate.typeStatus = false
  }
}

setTimeout(typeText, typeAnimate.newTextDelay)

const isAccept = ref(false)
const imageStore = useImageStore()
const isServerRun = ref(false)
const router = useRouter()
const url = import.meta.env.VITE_URL_API
const rules = computed(() => {
  return {
    prefix: { required: helpers.withMessage('โปรดระบุ', required) },
    name: { required: helpers.withMessage('กรุณากรอกชื่อ', required) },
    birthDate: { required: helpers.withMessage('กรุณากรอกวันเกิด', required) },
    phone: {
      required: helpers.withMessage('กรุณากรอกเบอร์โทร', required),
      numeric: helpers.withMessage('กรุณากรอกเป็นตัวเลขเท่านั้น', numeric),
      minLength: helpers.withMessage('กรุณากรอกเบอร์โทร 10 ตัว', minLength(10))
    },
    id: {
      required: helpers.withMessage('กรุณากรอกเลขบัตรประชาชน', required),
      numeric: helpers.withMessage('กรุณากรอกเป็นตัวเลขเท่านั้น', numeric),
      minLength: helpers.withMessage('กรุณากรอกเลขบัตร 13 ตัว', minLength(13))
    },
    address: { required: helpers.withMessage('กรุณากรอกที่อยู่', required) },
    zipCode: {
      required: helpers.withMessage('กรุณากรอกรหัสไปรษณีย์', required),
      numeric: helpers.withMessage('กรุณากรอกเป็นตัวเลขเท่านั้น', numeric),
      minLength: helpers.withMessage('กรุณากรอกรหัสไปรษณีย์ 5 ตัว', minLength(5))
    }
  }
})
const registerForm = ref({
  prefix: '',
  name: '',
  birthDate: '',
  phone: '',
  id: '',
  address: '',
  zipCode: ''
})
let $v = useVuelidate(rules, registerForm.value)
onMounted(async () => {
  await axios
    .get(`${url}/test`)
    .then((res) => {
      if (res.status === 200) isServerRun.value = true
      else
        toast('ระบบยังไม่พร้อมใช้งานในขณะนี้ โปรดสมัครผ่านช่องทาง line', {
          theme: 'auto',
          type: 'error',
          closeOnClick: false,
          pauseOnHover: false,
          autoClose: false,
          toastStyle: {
            fontFamily: 'kanit',
            color: '#070F2B'
          }
        })
    })
    .catch(() =>
      toast('ระบบยังไม่พร้อมใช้งานในขณะนี้ โปรดสมัครผ่านช่องทาง line', {
        theme: 'auto',
        type: 'error',
        closeOnClick: false,
        pauseOnHover: false,
        autoClose: false,
        toastStyle: {
          fontFamily: 'kanit',
          color: '#070F2B'
        }
      })
    )

  const data = localStorage.getItem('dataForm')
  if (data) {
    registerForm.value = JSON.parse(data)
    $v = useVuelidate(rules, registerForm.value)
    const check = await checkBlobURL(await imageStore.cardImgPreview)
    if (!check) registerForm.value.idCard = null
  }
})

watch(
  () => registerForm.value,
  async () => localStorage.setItem('dataForm', JSON.stringify(registerForm.value)),
  { deep: true }
)

const submitForm = async () => {
  const result = await $v.value.$validate()

  if (result && isAccept.value) return router.push({ name: 'payment' })
  if (!isAccept.value)
    toast('โปรดยินยอมแบบสอบถามนี้ ก่อนไปหน้าต่อไป', {
      theme: 'auto',
      type: 'error',
      toastStyle: {
        fontFamily: 'kanit',
        color: '#070F2B'
      }
    })
  if (!result)
    toast('เกิดข้อผิดพลาด โปรดตรวจสอบข้อมูลของท่าน', {
      theme: 'auto',
      type: 'error',
      toastStyle: {
        fontFamily: 'kanit',
        color: '#070F2B'
      }
    })
}
</script>

<template>
  <div id="navbar" class="navbar text-white px-5 bg-primary-200 flex justify-between">
    <button class="btn btn-ghost m-0 p-0 overflow-hidden">
      <LogoGiffarine class="h-[80px] overflow-hidden" />
    </button>
    <div class="font-light">
      <a href="#form" class="font-light">
        <button class="btn btn-ghost font-light btn-xs font-inter text-xss">สมัครสมาชิก</button>
      </a>
      <a href="https://lin.ee/mXAFYKj" target="_blank">
        <button class="btn btn-ghost font-light btn-xs font-inter text-xss">สอบถาม</button>
      </a>
    </div>
  </div>
  <div id="hero" class="flex">
    <div id="intorduce" class="pl-[2%] min-h-[380px] w-1/2 text-primary-200 gap-2 flex flex-col">
      <h1 class="text-lg">
        <span class="typed-text">{{ typeAnimate.typeValue }}</span>
        <span class="blinking-cursor">|</span>
        <span class="cursor">&nbsp;</span>
      </h1>
      <div class="flex font-light flex-col gap-3">
        <p>สิทธิพิเศษ <span class="text-red-500 font-bold bold text-sm">แคตตาล็อกฟรี!!</span></p>
        <div class="flex flex-col w-full gap-2 text-white">
          <BoxText
            class="animate-fade-right animate-once animate-duration-1000 animate-ease-out"
            text="ส่วนลด 25%"
          />
          <BoxText
            class="animate-fade-right animate-once animate-duration-1000 animate-ease-out"
            text="เงินปันผลทุกเดือน"
          />
          <BoxText
            class="animate-fade-right animate-once animate-duration-1000 animate-ease-out"
            text="ท่องเที่ยวต่างประเทศ"
          />
          <BoxText
            class="animate-fade-right animate-once animate-duration-1000 animate-ease-out"
            text="โบนัส 60,000 ฿ สิ้นปี"
          />
        </div>
        <p class="text-md font-normal">
          เพียง 180฿<br />
          ตลอดชีพ!!
        </p>
      </div>
    </div>
    <div></div>
    <div id="main-image" class="w-1/2 relative">
      <EllipseBackground
        class="absolute animate-fade animate-once animate-duration-1000 animate-ease-in-out bottom-0 right-0"
      />
      <div class="bottom-[0%] rounded-bl-[45%] w-max overflow-hidden absolute right-0">
        <NawaImg class="animate-fade-left animate-once animate-duration-1000 animate-ease-in-out" />
      </div>
    </div>
  </div>
  <div id="register" class="flex flex-col p-[2%]">
    <div
      class="flex text-sm items-center gap-4 text-primary-100 animate-flip-up animate-once animate-duration-1000 animate-ease-in-out"
    >
      <hr class="flex-1 border-primary-100" />
      <p id="form" class="flex-3">สมัครสมาชิก</p>
      <hr class="flex-1 border-primary-100" />
    </div>

    <div class="flex flex-col gap-2 text-primary-100">
      <div class="flex gap-1">
        <label
          class="flex flex-col w-[25%] animate-fade-up animate-once animate-duration-1000 animate-ease-in-out"
        >
          <p>คำนำหน้า <span class="text-red-400">*</span></p>
          <select
            v-model="registerForm.prefix"
            class="select bg-white border-[1px] border-gray-200 select-sm"
            :value="registerForm.prefix"
          >
            <option value="" disabled selected>ระบุ</option>
            <option value="นาย">นาย</option>
            <option value="นาง">นาง</option>
            <option value="นางสาว">นางสาว</option>
            <option value="ไม่ระบุ">ไม่ระบุ</option>
          </select>
          <div>
            <span
              v-for="error in $v.prefix.$errors"
              :key="error.$uid"
              class="text-red-500 ml-1 text-[13px]"
              >{{ error.$message }}
            </span>
          </div>
        </label>

        <InputText
          class="flex-auto animate-fade-up animate-once animate-duration-1000 animate-ease-in-out"
          v-model="registerForm.name"
          title="ชื่อ-นามสกุล"
          placeholder="สมศรี แสนสุขดี"
          :errors="$v.name.$errors"
          max-length="100"
        />
      </div>

      <div id="date-phone" class="flex gap-2 w-full text-primary-100">
        <InputText
          v-model="registerForm.birthDate"
          class="flex-1 animate-fade-up animate-once animate-duration-1000 animate-delay-500 animate-ease-in-out"
          title="วันเกิด (ปี พ.ศ.)"
          type="date"
          :errors="$v.birthDate.$errors"
        />
        <InputText
          v-model="registerForm.phone"
          type="tel"
          class="flex-1 animate-fade-up animate-once animate-duration-1000 animate-delay-500 animate-ease-in-out"
          title="เบอร์โทร"
          placeholder="082xxxxxxx"
          max-length="10"
          :errors="$v.phone.$errors"
        />
      </div>
      <InputText
        class="animate-fade-up animate-once animate-duration-1000 animate-delay-1000 animate-ease-in-out"
        v-model="registerForm.id"
        title="เลขบัตรประชาชน"
        type="tel"
        placeholder="111184xxxxxxx"
        max-length="13"
        :errors="$v.id.$errors"
      />

      <label
        class="w-full relative animate-fade-up animate-once animate-duration-1000 animate-delay-[1500ms] animate-ease-in-out"
      >
        <p
          :class="registerForm.address.length >= 300 && 'text-red-600'"
          class="absolute z-40 top-[33px] right-[10px] text-gray-400 text-[12px]"
        >
          {{ registerForm.address.length }}/300
        </p>
        <p>ที่อยู่ปัจจุบัน (จะมีการจัดส่งเอกสารให้) <span class="text-red-400">*</span></p>
        <textarea
          maxlength="300"
          v-model="registerForm.address"
          :errors="$v.address.$errors"
          class="textare bg-white relative border-[1px] pr-[60px] border-gray-200 rounded-lg p-1 w-full"
          placeholder="จังหวัด อำเภอ ตำบล หมู่ ถนน บ้านเลขที่"
        >
        </textarea>
        <div>
          <span
            v-for="error in $v.address.$errors"
            :key="error.$uid"
            class="text-red-500 ml-1 text-[13px]"
            >{{ error.$message }}
          </span>
        </div>
      </label>

      <InputText
        class="animate-fade-up animate-once animate-duration-1000 animate-delay-[2000ms] animate-ease-in-out"
        v-model="registerForm.zipCode"
        title="รหัสไปรษณีย์"
        placeholder="xxxxx"
        max-length="5"
        :errors="$v.zipCode.$errors"
      />

      <div
        class="flex gap-2 items-center animate-fade-up animate-once animate-duration-1000 animate-delay-[2500ms] animate-ease-in-out"
      >
        <input type="checkbox" v-model="isAccept" class="checkbox checkbox-sm border-gray-400" />
        <p>
          ข้าพเจ้ายอมรับเงื่อนไขของ
          <span onclick="pdpa.showModal()" class="text-blue-700 link">นโยบายการขอข้อมูล</span>
        </p>
      </div>
      <p v-show="!isServerRun" class="text-red-500 text-center">
        ระบบยังไม่พร้อมใช้งานในขณะนี้ ขออภัยในความไม่สะดวก
        <a target="_blank" class="text-blue-700 cursor-pointer link" href="https://lin.ee/mXAFYKj"
          >โปรดสมัครผ่านช่องทาง line</a
        >
      </p>
      <BtnForm :is-disabled="!isServerRun" @click="submitForm" text="สมัครสมาชิก" />
    </div>
    <dialog id="pdpa" class="modal">
      <div class="modal-box text-primary-100 bg-white font-kanit max-w-5xl">
        <h3 class="text-lg">นโยบายการขอข้อมูล</h3>
        <p class="py-4">
          แบบฟอร์มนี้จัดทำเพื่อสำหรับใช้ในการสมัครกิฟฟารีนเท่านั้น
          โดยจะไม่นำข้อมูลนี้ไปเผยแพร่ให้กับผู้ใดหรือนำไปทำธุรกรรมอื่นโดยข้อมูล
          รูปภาพบัตรประชาชนของท่านจะถูกลบออกภายในหนึ่งสัปดาห์หลังจากมีการส่งมาเพื่อความปลอดภัยกับข้อมูลของท่าน
          หากมีข้อสงสัย
          <a
            class="text-blue-700 cursor-pointer link"
            target="_blank"
            href="https://lin.ee/mXAFYKj "
            >โปรดติดต่อสอบถามผ่าน line</a
          >
        </p>
      </div>
      <form method="dialog" class="modal-backdrop">
        <button>close</button>
      </form>
    </dialog>
  </div>
</template>

<style>
.blinking-cursor {
  color: #2c3e50;
  -webkit-animation: 1s blink step-end infinite;
  -moz-animation: 1s blink step-end infinite;
  -ms-animation: 1s blink step-end infinite;
  -o-animation: 1s blink step-end infinite;
  animation: 1s blink step-end infinite;
}
@keyframes blink {
  from,
  to {
    color: transparent;
  }
  50% {
    color: #2c3e50;
  }
}
@-moz-keyframes blink {
  from,
  to {
    color: transparent;
  }
  50% {
    color: #2c3e50;
  }
}
@-webkit-keyframes blink {
  from,
  to {
    color: transparent;
  }
  50% {
    color: #2c3e50;
  }
}
@-ms-keyframes blink {
  from,
  to {
    color: transparent;
  }
  50% {
    color: #2c3e50;
  }
}
@-o-keyframes blink {
  from,
  to {
    color: transparent;
  }
  50% {
    color: #2c3e50;
  }
}
</style>
