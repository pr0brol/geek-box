    public class Main extends Window {
        public static void main(String[] args) throws Exception {
            new NettyClient("localhost", 8888).run();
        }
    }
