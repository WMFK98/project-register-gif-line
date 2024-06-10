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

import { useImageStore } from '@/store/imageStore'
import { storeToRefs } from 'pinia'

const dataForm = ref()
const imageStore = useImageStore()
const { paymentImg } = storeToRefs(imageStore)

const checkData = () =>
  dataForm.value?.prefix &&
  dataForm.value?.name &&
  dataForm.value?.phone &&
  dataForm.value?.birthDate &&
  dataForm.value?.address &&
  dataForm.value?.zipCode &&
  dataForm.value?.id

onMounted(async () => {
  dataForm.value = await JSON.parse(localStorage.getItem('dataForm'))
  const haveImge = await checkBlobURL(dataForm.value?.idCard?.preview)
  if (!haveImge || !checkData()) router.push({ name: 'register' })
})
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

const submitPayment = async () => {
  const result = await $v.value.$validate()
  if (result) router.push({ name: 'validate' })
}

const $v = useVuelidate(rules, paymentImg.value)
</script>

<template>
  <div class="flex flex-col p-3 items-center text-primary-100">
    <div class="flex flex-col items-center w-full gap-2">
      <h1 class="text-lg">ยืนยันการชำระเงิน</h1>
      <QRCode class="h-[250px]" />
      <div class="flex flex-col gap-2">
        <p>พร้อมเพย์ : 092-469-9587</p>
        <hr />
        <p>ธนาคาร : ไทยพาณิชย์</p>
        <p>หมายเลขบัญชี : 266-215-4166</p>
        <p>ชื่อ : นวพรรณ์ เเซ่ตั้ง</p>
        <p>จำนวนเงิน 180 บาท</p>

        <InputFile title="รูปหลักฐานการโอน" :img-stored="paymentImg" :errors="$v.img.$errors" />
        <div class="flex justify-center items-center h-[350px]">
          <img
            class="max-w-[100%] max-h-[100%]"
            :src="imageStore.paymentPreview || exPayment"
            alt=""
          />
        </div>
      </div>

      <div class="flex w-full justify-around">
        <BtnForm text="ต่อไป" @click="submitPayment" /><BtnForm
          @click="$router.push({ name: 'register' })"
          class="text-sm"
          text="แก้ไข"
        />
      </div>
    </div>
  </div>
</template>
