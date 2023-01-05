import Cookies from "js-cookie";
import Layout from "@/layout/Layout";
import router1 from "@/router/index";


export function activeRouter() {
    let userStr = Cookies.get('user')
    if (userStr){
        let user = JSON.parse(userStr)
        let root = {
            path: '/',
            name: 'Layout',
            component: Layout,
            redirect: "/person",
            children: []
        }
        user.permissionList.forEach(p => {
            let obj = {
                path: p.path,
                name: p.name,
                component: ()=> import("../views/"+p.name)
            };
            root.children.push(obj)
        })
        if (router1){
            router1.addRoute(root)
        }
    }
}