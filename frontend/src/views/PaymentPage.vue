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
import { useImageStore } from '@/store/imageStore'
import { storeToRefs } from 'pinia'
const url = import.meta.env.VITE_URL_API
const isLoading = ref(false)
const dataForm = ref()
const imageStore = useImageStore()
const { paymentImg, cardImg } = storeToRefs(imageStore)

const checkData = () =>
  dataForm.value?.prefix &&
  dataForm.value?.name &&
  dataForm.value?.phone &&
  dataForm.value?.birthDate &&
  dataForm.value?.address &&
  dataForm.value?.zipCode &&
  dataForm.value?.id

onMounted(async () => {
  const urlImage = await imageStore.cardImgPreview
  if (!urlImage) router.push({ name: 'register' })
  dataForm.value = await JSON.parse(localStorage.getItem('dataForm'))
  const haveImge = await checkBlobURL(dataForm.value?.idCard?.preview)

  if (!haveImge || !checkData()) router.push({ name: 'register', hash: '#error' })
})

const payment = ref({ img: null })
const router = useRouter()
const checkTypeFile = () => {
  const type = paymentImg.value.type
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
    const { id, name, prefix, birthDate, zipCode, phone, address } = await dataForm.value
    const formData = new FormData()
    formData.append('id', id)
    formData.append('name', name)
    formData.append('prefix', prefix)
    formData.append('birthDate', birthDate)
    formData.append('phone', phone)
    formData.append('address', address)
    formData.append('zipCode', zipCode)
    formData.append('cardImg', cardImg.value)
    formData.append('paymentImg', paymentImg.value)
    const response = await toast
      .promise(
        axios.post(`${url}/broadcast`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }),
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
      .catch(() => setTimeout(() => router.push({ name: 'register' }), 5000))

    if (response.status === 200) {
      localStorage.clear()
      imageStore.clearAll()
      isLoading.value = false
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
      <div class="flex flex-col mx-2 gap-2 w-screen sm:w-max px-3">
        <p>พร้อมเพย์ : 092-469-9587</p>
        <hr />
        <p>ธนาคาร : ไทยพาณิชย์</p>
        <p>หมายเลขบัญชี : 266-215-4166</p>
        <p>ชื่อ : นวพรรณ์ เเซ่ตั้ง</p>
        <p>จำนวนเงิน 180 บาท</p>

        <InputFile title="รูปหลักฐานการโอน" :img-stored="paymentImg" :errors="$v.img.$errors" />
        <div class="h-[350px] flex justify-center items-center">
          <img class="sm:h-[90%]" :src="imageStore.paymentPreview || exPayment" alt="" />
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
