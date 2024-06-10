/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      fontFamily: {
        kanit: ['"Inter"', '"Kanit"']
      },
      fontSize: {
        xss: '14px',
        xs: '16px',
        sm: '18px',
        md: '20px',
        lg: '26px'
      },

      colors: {
        'primary-100': '#070F2B',
        'primary-200': '#1B1A55',
        'primary-300': '#535C91',
        'primary-400': '#9290C3'
      }
    }
  },
  plugins: [require('daisyui'), require('tailwindcss-animated')]
}
