import Element from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

export default ({ Vue, isServer }) => {
    Vue.use(Element);
    if (!isServer) {
        import('vue-toasted' /* webpackChunkName: "notification" */).then((module) => {
            Vue.use(module.default)
        })
    }
}