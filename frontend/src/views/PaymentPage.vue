<script setup>
import InputFile from './../components/InputFile.vue'
import BtnForm from './../components/BtnForm.vue'
import QRCode from './../components/images/QRCode.vue'
import exPayment from '/images/ex-payment.jpeg'
import { computed, onMounted, ref } from 'vue'
import { useVuelidate } from '@vuelidate/core'
import { helpers } from '@vuelidate/validators'
import { useRouter } from 'vue-router'
import { checkBlobURL } from './../libs/previewBinary'
import { toast } from 'vue3-toastify'

import axios from 'axios'
const url = import.meta.env.VITE_URL_API
const isLoading = ref(false)
const dataForm = ref()
const checkData = () =>
  dataForm.value?.prefix &&
  dataForm.value?.name &&
  dataForm.value?.phone &&
  dataForm.value?.birthDate &&
  dataForm.value?.address &&
  dataForm.value?.zipCode &&
  dataForm.value?.id

onMounted(async () => {
  dataForm.value = JSON.parse(localStorage.getItem('dataForm'))
  const haveImge = await checkBlobURL(dataForm.value?.idCard?.preview)

  if (!haveImge || !checkData()) router.push({ name: 'register', hash: '#error' })
})

const payment = ref({ img: null })
const router = useRouter()
const checkTypeFile = (value) => {
  const type = value.type
  return type.includes('jpg') || type.includes('png') || type.includes('jpeg')
}
const rules = computed(() => {
  return {
    img: {
      checkTypeFile: helpers.withMessage('กรุณาใส่รูปภาพ สกุล (*.jpg,*.png,.*.jpeg)', checkTypeFile)
    }
  }
})

const submitForm = async () => {
  const result = await $v.value.$validate()
  if (result && checkData()) {
    const { id, name, prefix, birthDate, phone, address, zipCode } = dataForm.value
    const data = {
      id,
      name,
      prefix,
      birthDate,
      phone,
      address,
      zipCode
    }
    isLoading.value = true
    const response = await toast.promise(
      axios.post(`${url}/broadcast`, data),
      {
        pending: 'ระบบกำลังนำส่งข้อมูลของคุณ',
        success: 'ขอบคุณที่ร่วมเป็นส่วนหนึ่งกับทางเรา👏',
        error: 'เกิดข้อผิดพลาด โปรดลองอีกครั้งในภายหลัง'
      },
      {
        toastStyle: {
          fontFamily: 'kanit',
          color: '#070F2B'
        }
      }
    )
    isLoading.value = false
    if (response.status === 200) {
      localStorage.clear()
      router.push({ name: 'register' })
    }
  }
}

const $v = useVuelidate(rules, payment.value)
</script>

<template>
  <div class="flex flex-col p-5 items-center text-primary-100">
    <div class="flex flex-col items-center gap-2">
      <h1 class="text-lg">ยืนยันการชำระเงิน</h1>
      <QRCode class="h-[250px]" />
      <div class="flex flex-col gap-2">
        <p>พร้อมเพย์ : 092-469-9587</p>
        <hr />
        <p>ธนาคาร : ไทยพาณิชย์</p>
        <p>หมายเลขบัญชี : 266-215-4166</p>
        <p>ชื่อ : นวพรรณ์ เเซ่ตั้ง</p>
        <p>จำนวนเงิน 180 บาท</p>

        <InputFile title="รูปหลักฐานการโอน" v-model="payment.img" :errors="$v.img.$errors" />
        <div class="h-[350px] flex justify-center items-center">
          <img class="h-[90%]" :src="payment.img?.preview || exPayment" alt="test" />
        </div>
      </div>

      <div class="flex gap-3">
        <BtnForm :is-disabled="isLoading" text="ยืนยัน" @click="submitForm" /><BtnForm
          @click="$router.back()"
          :is-disabled="isLoading"
          class="text-sm"
          text="ย้อนกลับ"
        />
      </div>
    </div>
  </div>
</template>
