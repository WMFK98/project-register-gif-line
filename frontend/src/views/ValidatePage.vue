<script setup>
import { useRouter } from 'vue-router'
import BtnForm from './../components/BtnForm.vue'
import { checkBlobURL } from './../libs/previewBinary'
import { computed, onMounted, ref } from 'vue'
import { useImageStore } from '@/store/imageStore'
import InputFile from './../components/InputFile.vue'
import { storeToRefs } from 'pinia'
import { helpers } from '@vuelidate/validators'
import 'vue3-toastify/dist/index.css'
import exIdCard from '/images/ex-id-card.png'
import { toast } from 'vue3-toastify'
import axios from 'axios'
import useVuelidate from '@vuelidate/core'
const url = import.meta.env.VITE_URL_API
const isLoading = ref(false)
const checkData = () =>
  dataForm.value?.prefix &&
  dataForm.value?.name &&
  dataForm.value?.phone &&
  dataForm.value?.birthDate &&
  dataForm.value?.address &&
  dataForm.value?.zipCode &&
  dataForm.value?.id

const imageStore = useImageStore()
const { cardImg, paymentImg } = storeToRefs(imageStore)
const dataForm = ref()
const router = useRouter()
const checkTypeFile = () => {
  const type = cardImg.value.type
  return type.includes('jpg') || type.includes('png') || type.includes('jpeg')
}

const rules = computed(() => {
  return {
    img: {
      checkTypeFile: helpers.withMessage('กรุณาใส่รูปภาพ สกุล (*.jpg,*.png,.*.jpeg)', checkTypeFile)
    }
  }
})
const $v = useVuelidate(rules, cardImg.value)
const submitForm = async () => {
  isLoading.value = true
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
      .catch(() => {
        setTimeout(() => router.push({ name: 'register' }), 5000)
      })
    if (response.status === 200) {
      localStorage.clear()
      imageStore.clearAll()
      router.push({ name: 'register' })
    }
  }
}

onMounted(async () => {
  const urlImage = await imageStore.paymentPreview
  if (!urlImage) router.push({ name: 'register' })
  dataForm.value = JSON.parse(localStorage.getItem('dataForm'))
  const haveImge = await checkBlobURL(urlImage)
  const checkData = () =>
    dataForm.value?.prefix &&
    dataForm.value?.name &&
    dataForm.value?.phone &&
    dataForm.value?.birthDate &&
    dataForm.value?.address &&
    dataForm.value?.zipCode &&
    dataForm.value?.id
  if (!haveImge || !checkData()) router.push({ name: 'register' })
})
</script>

<template>
  <div class="flex flex-col pt-4 items-center text-primary-100">
    <div class="flex flex-col h-auto gap-2 justify-between">
      <div class="flex flex-col gap-2 w-screen px-3 sm:w-max">
        <h1 class="text-lg">โปรดตรวจสอบข้อมูลของท่าน</h1>
        <hr />
        <InputFile
          title="อัพโหลดรูปภาพบัตรประชาชน"
          :errors="$v.img.$errors"
          :img-stored="cardImg"
        />
        <div class="h-[350px] flex justify-center items-center">
          <img
            class="h-[90%]"
            :src="imageStore.cardImgPreview || exIdCard"
            :alt="cardImg?.name || 'exmple'"
          />
        </div>

        <div class="flex flex-col gap-2 justify-between flex-auto">
          <hr />
          <p>
            ชื่อ-นามสกุล : {{ dataForm?.prefix === 'ไม่ระบุ' ? '' : dataForm?.prefix }}
            {{ dataForm?.name }}
          </p>
          <p>หมายเลขบัตร : {{ dataForm?.id }}</p>
          <p>เบอร์โทร : {{ dataForm?.phone }}</p>
          <p>วันเกิด : {{ dataForm?.birthDate }}</p>
          <p>ที่อยู่ : {{ dataForm?.address }}</p>
          <p>รหัสไปรษณีย์ :{{ dataForm?.zipCode }}</p>
        </div>
      </div>
      <div class="flex gap-3">
        <BtnForm :is-disabled="isLoading" @click="submitForm" class="" text="ยืนยัน" />
        <BtnForm
          :is-disabled="isLoading"
          @click="$router.push({ name: 'payment' })"
          class="text-sm"
          text="ย้อนกลับ"
        />
      </div>
    </div>
  </div>
</template>
