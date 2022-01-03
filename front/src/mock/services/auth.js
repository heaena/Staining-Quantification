import Mock from 'mockjs2'
import { builder } from '../util'

const smsCaptcha = () => {
  return builder({ captcha: Mock.mock('@integer(10000, 99999)') })
}

const twofactor = () => {
  return builder({ stepCode: Mock.mock('@integer(0, 1)') })
}

Mock.mock(/\/account\/sms/, 'post', smsCaptcha)
Mock.mock(/\/auth\/2step-code/, 'post', twofactor)
