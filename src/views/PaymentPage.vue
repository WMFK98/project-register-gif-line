<script setup>
import InputFile from './../components/InputFile.vue'
import BtnForm from './../components/BtnForm.vue'
import QRCode from './../components/images/QRCode.vue'
</script>

<template>
  <div class="flex flex-col p-5 items-center gap-2 text-primary-100">
    <h1 class="text-lg">ยืนยันการชำระเงิน</h1>
    <QRCode class="h-[250px]" />
    <div class="flex flex-col gap-2">
      <p>พร้อมเพย์ : 092-469-9587</p>
      <hr />
      <p>ธนาคาร : ไทยพาณิชย์</p>
      <p>หมายเลขบัญชี : 266-215-4166</p>
      <p>ชื่อ : นวพรรณ์ เเซ่ตั้ง</p>
      <p>จำนวนเงิน 180 บาท</p>

      <InputFile title="รูปหลักฐานการโอน" />
    </div>

    <div class="flex gap-3">
      <BtnForm class="" text="ยืนยัน" /><BtnForm class="text-sm" text="ตรวจสอบ" />
    </div>
  </div>
</template>
