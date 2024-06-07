<script setup>
import { useRouter } from 'vue-router'
import BtnForm from './../components/BtnForm.vue'
import { checkBlobURL } from './../libs/previewBinary'
import { onMounted, ref } from 'vue'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
const dataForm = ref()
const router = useRouter()
onMounted(async () => {
  dataForm.value = JSON.parse(localStorage.getItem('dataForm'))
  const haveImge = await checkBlobURL(dataForm.value.idCard.preview)
  const checkData = () =>
    dataForm.value?.prefix &&
    dataForm.value?.name &&
    dataForm.value?.phone &&
    dataForm.value?.birthDate &&
    dataForm.value?.address &&
    dataForm.value?.zipCode &&
    dataForm.value?.idCard
  if (!haveImge || !checkData()) router.push({ name: 'register' })
})
</script>

<template>
  <div class="flex flex-col pt-4 items-center text-primary-100">
    <div class="flex flex-col h-auto gap-2 justify-between">
      <div class="flex flex-col gap-2">
        <h1 class="text-lg">โปรดตรวจสอบข้อมูลของท่าน</h1>
        <hr />
        <div class="flex flex-col gap-2">
          <p>รูปบัตรประชาชน</p>
          <div class="h-[300px] flex justify-center items-center">
            <img class="h-[90%]" :src="dataForm?.idCard.preview" alt="test" />
          </div>

          <img class="min-w-auto max-h-[250px]" alt="" />
        </div>

        <div class="flex flex-col gap-2 justify-between flex-auto">
          <hr />
          <p>
            ชื่อ-นามสกุล : {{ dataForm?.prefix === 'ไม่ระบุ' ? '' : dataForm?.prefix }}
            {{ dataForm?.name }}
          </p>
          <p>เบอร์โทร : {{ dataForm?.phone }}</p>
          <p>วันเกิด : {{ dataForm?.birthDate }}</p>
          <p>ที่อยู่ : {{ dataForm?.address }}</p>
          <p>รหัสไปรษณีย์ :{{ dataForm?.zipCode }}</p>
        </div>
      </div>

      <div class="flex gap-3">
        <BtnForm @click="$router.push({ name: 'payment' })" class="" text="ยืนยัน" />
        <BtnForm @click="$router.push({ name: 'register' })" class="text-sm" text="แก้ไข" />
      </div>
    </div>
  </div>
</template>
