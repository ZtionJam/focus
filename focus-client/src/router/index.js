import { createRouter, createWebHashHistory } from 'vue-router'
import mainPage from '@/pages/mainPage/mainPage'
const routes = [{
    path: "/",
    redirect: '/mainPage',
    meta: { title: '焦距' }
},
{
    path: "/mainPage",
    component: mainPage,
    meta: { title: '焦距' }
}
]
const router = createRouter({
    model:'hash',
    history: createWebHashHistory(process.env.BASE_URL),
    routes: routes
})
export default router