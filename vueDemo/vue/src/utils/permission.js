import Layout from "@/layout/Layout";
import router from "@/router";

export function activeRouter(permissions) {
    let root = {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: "/home",
        children: []
    }
    permissions.forEach(p => {
        let obj = {
            path: p.path,
            name: p.name,
            component: ()=> import("../views/"+p.name)
        };
        root.children.push(obj)
    })
    router.addRoute(root)
}