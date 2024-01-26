import { Resize } from "./resizeObserver"


// 自定义指令集合
const directives = {
    Resize,
}

export default {
    install(app) {
        Object.keys(directives).forEach((key) => {
            app.directive(key, directives[key])
        })
    }
}


// Compare this snippet from ui/src/main.js: