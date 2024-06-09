<script setup>
import LogoGiffarine from './../components/icons/LogoGiffarine.vue'
import NawaImg from './../components/images/NawaphanImg.vue'
import EllipseBackground from './../components/images/EllipseBackground.vue'
import BoxText from './../components/BoxText.vue'
import InputText from './../components/InputText.vue'
import InputFile from './../components/InputFile.vue'
import BtnForm from './../components/BtnForm.vue'
import exIdCard from '/images/ex-id-card.jpg'
import { checkBlobURL } from './../libs/previewBinary'
import { useVuelidate } from '@vuelidate/core'
import { required, minLength, helpers, numeric } from '@vuelidate/validators'
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { toast } from 'vue3-toastify'
import { useImageStore } from '@/store/imageStore'
import { storeToRefs } from 'pinia'
import axios from 'axios'
const imageStore = useImageStore()
const isServerRun = ref(false)
const { cardImg } = storeToRefs(imageStore)
const router = useRouter()
const url = import.meta.env.VITE_URL_API
const checkTypeFile = () => {
  const type = cardImg.value.type
  return type.includes('jpg') || type.includes('png') || type.includes('jpeg')
}
const rules = computed(() => {
  return {
    prefix: { required: helpers.withMessage('โปรดระบุ', required) },
    name: { required: helpers.withMessage('กรุณากรอกชื่อ', required) },
    birthDate: { required: helpers.withMessage('กรุณาวันเกิด', required) },
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
    },
    idCard: {
      checkTypeFile: helpers.withMessage('กรุณาใส่รูปภาพ สกุล (*.jpg,*.png,.*.jpeg)', checkTypeFile)
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
      if (res.status === 200) isServerRun.value === true
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
  if (result) router.push({ name: 'validate' })
  else
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
      <h1 class="text-lg">ยินดีต้อนรับสู่ การสมัครสมาชิก สมัครตัวแทน Giffarine</h1>
      <div class="flex font-light flex-col gap-3">
        <p>สิทธิพิเศษ</p>
        <div class="flex flex-col w-full gap-2 text-white">
          <BoxText text="ส่วนลด 25%" />
          <BoxText text="เงินปันผลทุกเดือน" />
          <BoxText text="ท่องเที่ยวต่างประเทศ" />
          <BoxText text="โบนัส 60,000 ฿ สิ้นปี" />
        </div>
        <p class="text-md font-normal">
          เพียง 180฿ <br />
          ตลอดชีพ!!
        </p>
      </div>
    </div>
    <div></div>
    <div id="main-image" class="w-1/2 relative">
      <EllipseBackground class="absolute bottom-0 right-0" />
      <div class="bottom-[0%] rounded-bl-[45%] w-max overflow-hidden absolute right-0">
        <NawaImg />
      </div>
    </div>
  </div>
  <div id="register" class="flex flex-col p-[2%] font-light">
    <div class="flex text-sm items-center gap-4 text-primary-100">
      <hr class="flex-1 border-primary-100" />
      <p id="form" class="flex-3">สมัครสมาชิก</p>
      <hr class="flex-1 border-primary-100" />
    </div>

    <div class="flex flex-col gap-2 text-primary-100">
      <div class="flex gap-1">
        <label class="flex flex-col w-[25%]">
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
          class="flex-auto"
          v-model="registerForm.name"
          title="ชื่อ-นามสกุล"
          placeholder="สมศรี แสนสุขดี"
          :errors="$v.name.$errors"
        />
      </div>

      <div id="date-phone" class="flex gap-2 w-full text-primary-100">
        <InputText
          v-model="registerForm.birthDate"
          class="flex-1"
          title="วันเกิด (ปี พ.ศ.)"
          type="date"
          :errors="$v.birthDate.$errors"
        />
        <InputText
          v-model="registerForm.phone"
          class="flex-1"
          title="เบอร์โทร"
          placeholder="082xxxxxxx"
          max-length="10"
          :errors="$v.phone.$errors"
        />
      </div>
      <InputText
        v-model="registerForm.id"
        title="เลขบัตรประชาชน"
        placeholder="111184xxxxxxx"
        max-length="13"
        :errors="$v.id.$errors"
      />

      <label class="w-full">
        <p>ที่อยู่ปัจจุบัน (จะมีการจัดส่งเอกสารให้) <span class="text-red-400">*</span></p>
        <textarea
          v-model="registerForm.address"
          :errors="$v.address.$errors"
          class="textare bg-white border-[1px] border-gray-200 rounded-lg p-1 w-full"
          placeholder="จังหวัด อำเภอ ตำบล หมู่ ถนน บ้านเลขที่"
        ></textarea>
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
        class=""
        v-model="registerForm.zipCode"
        title="รหัสไปรษณีย์"
        placeholder="xxxxx"
        max-length="5"
        :errors="$v.zipCode.$errors"
      />
      <InputFile
        title="อัพโหลดรูปภาพบัตรประชาชน"
        :img-stored="cardImg"
        :errors="$v.idCard.$errors"
      />

      <div class="h-[350px] flex justify-center items-center">
        <img
          class="h-[90%]"
          :src="imageStore.cardImgPreview || exIdCard"
          :alt="cardImg?.name || 'test'"
        />
      </div>
      <p v-show="!isServerRun" class="text-red-500 text-center">
        ระบบยังไม่พร้อมใช้งานในขณะนี้ ขออภัยในความไม่สะดวก
        <a class="text-blue-700 cursor-pointer link" href="https://lin.ee/mXAFYKj"
          >โปรดสมัครผ่านช่องทาง line</a
        >
      </p>
      <BtnForm :is-disabled="!isServerRun" @click="submitForm" text="สมัครสมาชิก" />
    </div>
  </div>
</template>
