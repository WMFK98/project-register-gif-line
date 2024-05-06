<script setup>
import LogoGiffarine from './components/icons/LogoGiffarine.vue'
import NawaImg from './components/images/NawaphanImg.vue'
import EllipseBackground from './components/images/EllipseBackground.vue'
import BoxText from './components/BoxText.vue'
import InputForm from './components/InputForm.vue'
import BtnForm from './components/BtnForm.vue'
</script>

<template>
  <div
    id="navbar"
    class="bg-white w-screen sticky flex flex-col gap-3 font-kanit min-h-screen overflow-hidden max-h-max font pb-1"
  >
    <div class="navbar text-white p-0 bg-primary-200 flex justify-between">
      <button class="btn btn-ghost m-0 p-0"><LogoGiffarine class="h-[80px]" /></button>

      <div class="font-light">
        <button class="btn btn-ghost font-light btn-xs font-inter text-xss">สมัครสมาชิก</button>
        <button class="btn btn-ghost font-light text-xss">สอบถาม</button>
      </div>
    </div>
    <div id="hero" class="flex">
      <div id="intorduce" class="pl-[2%] w-1/2 text-primary-200 gap-2 flex flex-col">
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
          <NawaImg class="" />
        </div>
      </div>
    </div>
    <div id="register" class="flex flex-col p-[2%] font-light">
      <div class="flex text-sm items-center gap-4 text-primary-100">
        <hr class="flex-1 border-primary-100" />
        <p class="flex-3">สมัครสมาชิก</p>
        <hr class="flex-1 border-primary-100" />
      </div>
      <div id="form" class="flex flex-col gap-2 text-primary-100">
        <InputForm title="ชื่อ-นามสกุล" placeholder="สมศรี แสนสุขดี" />

        <div id="date-phone" class="flex gap-2 w-full text-primary-100">
          <InputForm class="flex-1" title="วันเกิด" type="date" />
          <InputForm class="flex-1" title="เบอร์โทร" placeholder="082xxxxxxx" max-length="10" />
        </div>
        <InputForm title="เลขบัตรประชาชน" placeholder="111184xxxxxxx" max-length="13" />

        <label class="w-full">
          <p>ที่อยู่ปัจจุบัน (จะมีการจัดส่งเอกสารให้) <span class="text-red-400">*</span></p>
          <textarea
            class="textare bg-white border-[1px] border-gray-200 rounded-lg p-1 w-full"
            placeholder="จังหวัด อำเภอ ตำบล หมู่ ถนน บ้านเลขที่"
          ></textarea>
        </label>

        <label class="w-full">
          <p>อัพโหลดรูปภาพบัตรประชาชน <span class="text-red-400">*</span></p>
          <input
            type="file"
            class="file-input bg-slate-50 file-input-xs file-input-bordered w-full max-w-xs"
          />
        </label>
        <BtnForm text="สมัครสมาชิก" />
      </div>
    </div>
  </div>
</template>
