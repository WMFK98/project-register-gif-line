<script setup></script>

<template>
  <div class="w-screen min-h-screen max-h-max bg-white lg:bg-slate-500 py-0 lg:py-8">
    <div
      class="bg-white md:w-[580px] relative overflow-x-hidden rounded-none mx-auto md:rounded-lg overflow-hidden flex flex-col gap-3 font-kanit pb-1"
    >
      <RouterView />
      <a
        href="https://lin.ee/mXAFYKj"
        class="fixed w-[50px] rounded-full overflow-hidden lg:w-[60px] bottom-3 right-8"
        target="_blank"
      >
        <img class="" src="/images/line-icon.webp" alt="line-icon" />
      </a>
    </div>
  </div>
</template>
